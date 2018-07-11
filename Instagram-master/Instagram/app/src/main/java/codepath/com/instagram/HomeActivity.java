package codepath.com.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import codepath.com.instagram.Model.Post;

// parse-dashboard --appId mlinstagram --masterKey masterkey --serverURL http://melissalu8-instagram.herokuapp.com/parse

public class HomeActivity extends AppCompatActivity {

    private ImageButton createButton;
    private Button refreshButton;
    private Button logoutBtn;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if (savedInstanceState == null) {
//            fragmentA = FragmentA.newInstance("foo");
//            fragmentB = FragmentB.newInstance("bar");
//            fragmentC = FragmentC.newInstance("baz");
//
//            // Let's first dynamically add a fragment into a frame container
//            getSupportFragmentManager().beginTransaction().
//                    replace(R.id.your_placeholder, new Capture(), "AddCapture").
//                    commit();
//            // Now later we can lookup the fragment by tag
//            Capture capture = (Capture)
//                    getSupportFragmentManager().findFragmentByTag("CaptureLookUp");
//
//            capture.onLaunchCamera();
//        }
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tbTop);
//        setSupportActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Capture capture = new Capture();
//        final Fragment fragment2 = new SecondFragment();
//        final Fragment fragment3 = new ThirdFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            default:
                            case R.id.aCreate:
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.your_placeholder, capture).commit();
                                return true;
//                            case R.id.action_schedules:
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.flContainer, fragment2).commit();
//                                return true;
//                            case R.id.action_music:
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.flContainer, fragment3).commit();
//                                return true;
                        }
                    }
                });

        refreshButton = findViewById(R.id.btnRefresh);
        logoutBtn = findViewById(R.id.btnLogout);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "You have successfully logged out", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        Log.d("HomeActivity", "Post[" + i + "' = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

package com.tokopedia.core.welcome;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sromku.simple.fb.SimpleFacebook;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.welcome.view.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    private SimpleFacebook simplefacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if(savedInstanceState == null){
            Fragment fragment = WelcomeFragment.createInstance(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, WelcomeActivity.class.getSimpleName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        simplefacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        simplefacebook.onActivityResult(requestCode, resultCode, data);
    }

}

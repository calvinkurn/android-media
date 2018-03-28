package com.rizkyfadillah.gamificationapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.gamification.cracktoken.presentation.fragment.CrackTokenFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CrackTokenFragment crackTokenFragment = CrackTokenFragment.newInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, crackTokenFragment);
        fragmentTransaction.commit();
    }
}

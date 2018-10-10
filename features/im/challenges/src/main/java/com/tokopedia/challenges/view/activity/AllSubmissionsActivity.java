package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.challenges.view.fragments.AllSubmissionFragment;

public class AllSubmissionsActivity extends ChallengesBaseActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, AllSubmissionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = AllSubmissionFragment.createInstance(getIntent().getExtras());
        return fragment;
    }

}

package com.tokopedia.challenges.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;

/**
 * @author lalit.singh
 */
public class ChallengeDetailsActivity extends ChallengesBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return ChallengeDetailsFragment.createInstance(getIntent().getExtras());
    }

}

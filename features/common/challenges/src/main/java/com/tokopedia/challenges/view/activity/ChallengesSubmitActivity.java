package com.tokopedia.challenges.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitFragment;

public class ChallengesSubmitActivity extends BaseActivity {
    @Override
    protected Fragment getNewFragment() {
        return ChallengesSubmitFragment.newInstance();
    }
}

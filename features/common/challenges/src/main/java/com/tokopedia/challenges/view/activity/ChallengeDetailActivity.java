package com.tokopedia.challenges.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;

public class ChallengeDetailActivity extends BaseActivity {


    private ChallengesComponent challengesComponent;

    @Override
    protected Fragment getNewFragment() {
        return ChallegeneSubmissionFragment.createInstance(getIntent().getExtras());
    }
}

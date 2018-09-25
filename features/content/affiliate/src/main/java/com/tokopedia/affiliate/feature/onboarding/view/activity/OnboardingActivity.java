package com.tokopedia.affiliate.feature.onboarding.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.onboarding.view.fragment.OnboardingFragment;

public class OnboardingActivity extends BaseSimpleActivity {

    public static final String PARAM_IS_FINISH = "IS_FINISH";
    public static final Boolean FINISH_TRUE = true;

    public static Intent createIntent(Context context) {
        return createIntent(context, false);
    }

    public static Intent createIntent(Context context, Boolean isFinish) {
        Intent intent = new Intent(context, OnboardingActivity.class);
        intent.putExtra(PARAM_IS_FINISH, isFinish);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment =  OnboardingFragment.newInstance();
        if (getIntent().getExtras() != null) {
            fragment.setArguments(new Bundle(getIntent().getExtras()));
        }
        return fragment;
    }
}

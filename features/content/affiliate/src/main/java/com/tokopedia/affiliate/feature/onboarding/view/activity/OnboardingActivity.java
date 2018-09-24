package com.tokopedia.affiliate.feature.onboarding.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.onboarding.view.fragment.OnboardingFragment;

public class OnboardingActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return OnboardingFragment.newInstance();
    }
}

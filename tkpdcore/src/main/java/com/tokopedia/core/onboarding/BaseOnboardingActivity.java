package com.tokopedia.core.onboarding;

import android.content.pm.ActivityInfo;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;

/**
 * Created by hafizh HERDI on 3/7/2016.
 */
public abstract class BaseOnboardingActivity extends AppIntro {

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


}

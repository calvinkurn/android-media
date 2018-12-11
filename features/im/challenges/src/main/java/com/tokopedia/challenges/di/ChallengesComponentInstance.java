package com.tokopedia.challenges.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;


public class ChallengesComponentInstance {
    private static ChallengesComponent challengesComponent;

    public static ChallengesComponent getChallengesComponent(Application application) {
        if (challengesComponent == null) {
            challengesComponent = DaggerChallengesComponent.builder().baseAppComponent(
                    ((BaseMainApplication)application).getBaseAppComponent()).build();
        }
        return challengesComponent;
    }
}

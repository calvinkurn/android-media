package com.tokopedia.gamification.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

/**
 * Created by hendry on 02/04/18.
 */

public class GamificationComponentInstance {
    private static GamificationComponent gamificationComponent;

    public static GamificationComponent getComponent(Application application) {
        if (gamificationComponent == null) {
            gamificationComponent = DaggerGamificationComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return gamificationComponent;
    }
}

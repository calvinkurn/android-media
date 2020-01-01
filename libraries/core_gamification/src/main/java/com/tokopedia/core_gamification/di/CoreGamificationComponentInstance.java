package com.tokopedia.core_gamification.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

/**
 * Created by hendry on 02/04/18.
 */

public class CoreGamificationComponentInstance {
    private static CoreGamificationComponent gamificationComponent;

    public static CoreGamificationComponent getComponent(Application application) {
        if (gamificationComponent == null) {
            gamificationComponent = DaggerCoreGamificationComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return gamificationComponent;
    }
}

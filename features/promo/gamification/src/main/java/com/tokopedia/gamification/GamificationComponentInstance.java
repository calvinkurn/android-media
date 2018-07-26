package com.tokopedia.gamification;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.di.DaggerGamificationComponent;

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

package com.tokopedia.promogamification.common.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

/**
 * Created by hendry on 02/04/18.
 */

public class CommonGamificationComponentInstance {
    private static CommonGamificationComponent gamificationComponent;

    public static CommonGamificationComponent getComponent(Application application) {
        if (gamificationComponent == null) {
            gamificationComponent = DaggerCommonGamificationComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return gamificationComponent;
    }
}

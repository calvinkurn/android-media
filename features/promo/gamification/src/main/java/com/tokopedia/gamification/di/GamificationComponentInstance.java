package com.tokopedia.gamification.di;

import android.content.Context;

/**
 * Created by hendry on 02/04/18.
 */

public class GamificationComponentInstance {
    private static GamificationComponent gamificationComponent;

    public static GamificationComponent getComponent(Context context) {
        if (gamificationComponent == null) {
            gamificationComponent = DaggerGamificationComponent.builder()
                    .activityContextModule(new ActivityContextModule(context))
                    .build();
        }
        return gamificationComponent;
    }
}
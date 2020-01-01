package com.tokopedia.core_gamification;

/**
 * Created by nabillasabbaha on 4/4/18.
 */

public interface CoreGamificationEventTracking {

    interface Event {
        String CLICK_LUCKY_EGG = "luckyEggClick";
        String VIEW_LUCKY_EGG = "luckyEggView";
    }

    interface Category {
        String CLICK_LUCKY_EGG = "lucky egg - homepage entry point";
    }

    interface Action {
        String CLICK_LUCKY_EGG = "click egg on homepage";
        String IMPRESSION_LUCKY_EGG = "impression on egg";
    }
}

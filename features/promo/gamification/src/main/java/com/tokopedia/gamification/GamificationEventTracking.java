package com.tokopedia.gamification;

/**
 * Created by nabillasabbaha on 4/4/18.
 */

public interface GamificationEventTracking {

    interface Event {
        String CLICK_LUCKY_EGG = "luckyEggClick";
        String VIEW_LUCKY_EGG = "luckyEggView";
    }

    interface Category {
        String CLICK_LUCKY_EGG = "lucky egg - homepage entry point";
        String CRACK_LUCKY_EGG = "lucky egg - crack the egg";
        String VIEW_REWARD = "lucky egg - rewards page";
        String REWARD_CLICK = "lucky egg - ";
        String ERROR_PAGE = "lucky egg - error page";
        String EXPIRED_TOKEN = "lucky egg - error expired token";
        String EMPTY_PAGE = "lucky egg - empty page";
    }

    interface Action {
        String CLICK_LUCKY_EGG = "click egg on homepage";
        String IMPRESSION_LUCKY_EGG = "impression on egg";
        String IMPRESSION = "impression";
        String CRACK_LUCKY_EGG = "click on egg";
        String CLICK = "click";
        String CLICK_CLOSE_BUTTON = "click close button";
        String CLICK_OK = "click ok";
        String CLICK_TRY_AGAIN = "click coba lagi";
        String CLICK_LOYALTY_ICON = "click loyalty icon";
        String CLICK_KUPON_ICON = "click kupon icon";
        String CLICK_POINT_ICON = "click tokopoints icon";
        String CLICK_DAILY_PRIZE = "click daily prize";
        String VIEW_ERROR = "view error";
    }
}

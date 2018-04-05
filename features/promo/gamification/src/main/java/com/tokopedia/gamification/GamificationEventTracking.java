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
        String POINT_AND_LOYALTY_REWARD = "lucky egg - points and loyalty reward";
        String COUPON_REWARD = "lucky egg - coupon rewards";
        String ERROR_PAGE = "lucky egg - error page";
        String EXPIRED_TOKEN = "lucky egg - error expired token";
        String EMPTY_PAGE = "lucky egg - empty page";
    }

    interface Action {
        String CLICK_LUCKY_EGG = "click egg on homepage";
        String IMPRESSION_LUCKY_EGG = "impression";
        String CRACK_LUCKY_EGG = "click on egg";
        String CLICK_CRACK_OTHER_EGG = "click pecahkan telur lainnya";
        String CLICK_TO_TOKOPOINT = "click cek tokopoints";
        String CLICK_CLOSE_BUTTON = "click close button";
        String EMPTY_PAGE = "click belanja dan dapatkan lucky egg";
        String CLICK_USE_GIFT = "click cek dan gunakan hadiah";
    }
}

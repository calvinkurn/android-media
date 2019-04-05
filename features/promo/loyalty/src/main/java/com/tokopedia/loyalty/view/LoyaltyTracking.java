package com.tokopedia.loyalty.view;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

/**
 * Created by hendry on 21/03/19.
 */
public class LoyaltyTracking {

    public static final String EVENT_TOKOPOINT = "eventTokopoint";
    public static final String EVENT_LOYALTI_DIGITAL = "clickCoupon";

    public static void sendEventCouponPageClosed(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_TOKOPOINT,
                Category.TOKOPOINTS_LOYALTI,
                Action.CLICK_CLOSE_BTN,
                "close");
    }

    public static void sendEventMyCouponClicked(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_TOKOPOINT,
                Category.TOKOPOINTS_LOYALTI,
                Action.CLICK_MY_COUPON,
                "kupon saya");
    }

    public static void eventclickTabPromoCode(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOYALTI_DIGITAL,
                Category.DIGITAL_CHECKOUT,
                Action.CLICK_TAB_PROMO_CODE, categoryName.toLowerCase()));
    }

    public static void eventclickTabMyCoupon(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOYALTI_DIGITAL,
                Category.DIGITAL_CHECKOUT,
                Action.CLICK_MY_COUPON, categoryName.toLowerCase()));
    }

    public static void eventclickBtnFailedUsePromoCode(String categoryName, String errorMessage) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOYALTI_DIGITAL,
                Category.DIGITAL_CHECKOUT,
                Action.CLICK_BTN_USE_PROMO_CODE,
                categoryName.toLowerCase() + " - failed - " + errorMessage.toLowerCase()));
    }

    public static void eventclickBtnSuccessUsePromoCode(String categoryName, String promoCode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOYALTI_DIGITAL,
                Category.DIGITAL_CHECKOUT,
                Action.CLICK_BTN_USE_PROMO_CODE,
                categoryName.toLowerCase() + " - success - " + promoCode.toLowerCase()));
    }

    public static void eventclickBtnFailedUseCoupon(String categoryName, String errorMessage) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOYALTI_DIGITAL,
                Category.DIGITAL_CHECKOUT,
                Action.CLICK_COUPON,
                categoryName.toLowerCase() + " - failed - " + errorMessage.toLowerCase()));
    }

    public static void eventclickBtnSuccessUseCoupon(String categoryName, String promoCode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOYALTI_DIGITAL,
                Category.DIGITAL_CHECKOUT,
                Action.CLICK_COUPON,
                categoryName.toLowerCase() + " - success - " + promoCode.toLowerCase()));
    }

    static class Category {
        static String TOKOPOINTS_LOYALTI = "tokopoints - kode promo & kupon page";
        static String DIGITAL_CHECKOUT = "digital - checkout";
    }

    static class Action {
        static String CLICK_CLOSE_BTN = "click close button";
        static String CLICK_MY_COUPON = "click kupon saya";
        static String CLICK_TAB_PROMO_CODE = "click kode promo";
        static String VIEW_COUPON = "view kupon";
        static String CLICK_COUPON = "click kupon";
        static String CLICK_BTN_USE_PROMO_CODE = "click gunakan kode";
    }
}

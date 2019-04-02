package com.tokopedia.loyalty.view;

import com.tokopedia.track.TrackApp;

/**
 * Created by hendry on 21/03/19.
 */
public class LoyaltyTracking {

    public static final String EVENT_TOKOPOINT = "eventTokopoint";

    public static void sendEventCouponPageClosed(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_TOKOPOINT,
                "tokopoints - kode promo & kupon page",
                "click close button",
                "close");
    }

    public static void sendEventMyCouponClicked(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_TOKOPOINT,
                "tokopoints - kode promo & kupon page",
                "click kupon saya",
                "kupon saya");
    }
}

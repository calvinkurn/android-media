package com.tokopedia.navigation;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.navigation.GlobalNavConstant.Analytics.BOTTOM;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.CLICK;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.CLICK_HOMEPAGE;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.CLICK_HOME_PAGE;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.EVENT;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.EVENT_ACTION;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.EVENT_CATEGORY;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.EVENT_LABEL;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.HOME_PAGE;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.INBOX;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.NAV;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.NOTIFICATION;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.PAGE;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.SCREEN_NAME;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.SCREEN_NAME_CHAT;
import static com.tokopedia.navigation.GlobalNavConstant.Analytics.TOP_NAV;

/**
 * Created by meta on 03/08/18.
 */
public class GlobalNavAnalytics {

    @Inject
    GlobalNavAnalytics() {

    }

    public void eventBottomNavigation(String name) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_HOMEPAGE,
                String.format("%s %s %s", HOME_PAGE, BOTTOM, NAV),
                String.format("%s %s %s", CLICK, name.toLowerCase(), NAV),
                ""
        ));
    }

    public void eventNotificationPage(String section, String item) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_HOMEPAGE,
                String.format("%s %s", NOTIFICATION, PAGE),
                String.format("%s - %s - %s", CLICK, section, item),
                ""
        ));
    }

    public void eventInboxPage(String item) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_HOMEPAGE,
                String.format("%s %s", INBOX, PAGE),
                String.format("%s - %s", CLICK, item),
                ""
        ));
    }

    public void eventTrackingNotification() {

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, SCREEN_NAME_CHAT);
        eventTracking.put(EVENT, CLICK_HOME_PAGE);
        eventTracking.put(EVENT_CATEGORY, TOP_NAV);
        eventTracking.put(EVENT_ACTION, String.format("%s %s", CLICK, NOTIFICATION));
        eventTracking.put(EVENT_LABEL, "");

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(eventTracking);
    }

    public void eventImpressionAppUpdate(boolean isForceUpdate) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GlobalNavConstant.Analytics.EVENT_IMPRESSION_APP_UPDATE,
                GlobalNavConstant.Analytics.CATEGORY_APP_UPDATE,
                GlobalNavConstant.Analytics.IMPRESSION,
                isForceUpdate ?
                        GlobalNavConstant.Analytics.LABEL_FORCE_APP_UPDATE :
                        GlobalNavConstant.Analytics.LABEL_OPTIONAL_APP_UPDATE
        ));
    }

    public void eventClickAppUpdate(boolean isForceUpdate) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GlobalNavConstant.Analytics.EVENT_CLICK_APP_UPDATE,
                GlobalNavConstant.Analytics.CATEGORY_APP_UPDATE,
                GlobalNavConstant.Analytics.CLICK,
                isForceUpdate ?
                        GlobalNavConstant.Analytics.LABEL_FORCE_APP_UPDATE :
                        GlobalNavConstant.Analytics.LABEL_OPTIONAL_APP_UPDATE
        ));
    }

    public void eventClickCancelAppUpdate(boolean isForceUpdate) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GlobalNavConstant.Analytics.EVENT_CLICK_CANCEL_APP_UPDATE,
                GlobalNavConstant.Analytics.CATEGORY_APP_UPDATE,
                GlobalNavConstant.Analytics.CLICK,
                isForceUpdate ?
                        GlobalNavConstant.Analytics.LABEL_FORCE_CANCEL_APP_UPDATE :
                        GlobalNavConstant.Analytics.LABEL_OPTIONAL_CANCEL_APP_UPDATE
        ));
    }

    public void trackFirstTime(Context context) {
        ((GlobalNavRouter) context.getApplicationContext()).sendAnalyticsFirstTime();
        LocalCacheHandler cache = new LocalCacheHandler(context, GlobalNavConstant.Cache.KEY_FIRST_TIME);
        cache.putBoolean(GlobalNavConstant.Cache.KEY_IS_FIRST_TIME, true);
        cache.applyEditor();
    }

    public void eventClickNewestInfo() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                "clickNotifCenter",
                "notif center",
                "click on info terbaru",
                ""
        ));
    }
}

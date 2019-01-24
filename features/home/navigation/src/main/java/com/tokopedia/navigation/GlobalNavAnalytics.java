package com.tokopedia.navigation;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.navigation.GlobalNavConstant.Analytics.*;

/**
 * Created by meta on 03/08/18.
 */
public class GlobalNavAnalytics {

    private AnalyticTracker analyticTracker;

    @Inject
    GlobalNavAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventBottomNavigation(String name) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                CLICK_HOMEPAGE,
                String.format("%s %s %s", HOME_PAGE, BOTTOM, NAV),
                String.format("%s %s %s", CLICK, name.toLowerCase(), NAV),
                ""
        );
    }

    public void eventNotificationPage(String section, String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                CLICK_HOMEPAGE,
                String.format("%s %s", NOTIFICATION, PAGE),
                String.format("%s - %s - %s", CLICK, section, item),
                ""
        );
    }

    public void eventInboxPage(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                CLICK_HOMEPAGE,
                String.format("%s %s", INBOX, PAGE),
                String.format("%s - %s", CLICK, item),
                ""
        );
    }

    public void eventTrackingNotification() {
        if (analyticTracker == null)
            return;

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, SCREEN_NAME_CHAT);
        eventTracking.put(EVENT, CLICK_HOME_PAGE);
        eventTracking.put(EVENT_CATEGORY, TOP_NAV);
        eventTracking.put(EVENT_ACTION, String.format("%s %s", CLICK, NOTIFICATION));
        eventTracking.put(EVENT_LABEL, "");

        analyticTracker.sendEventTracking(eventTracking);
    }

    public void eventImpressionAppUpdate(boolean isForceUpdate) {
        analyticTracker.sendEventTracking(
                GlobalNavConstant.Analytics.EVENT_IMPRESSION_APP_UPDATE,
                GlobalNavConstant.Analytics.CATEGORY_APP_UPDATE,
                GlobalNavConstant.Analytics.IMPRESSION,
                isForceUpdate ?
                        GlobalNavConstant.Analytics.LABEL_FORCE_APP_UPDATE :
                        GlobalNavConstant.Analytics.LABEL_OPTIONAL_APP_UPDATE
        );
    }

    public void eventClickAppUpdate(boolean isForceUpdate) {
        analyticTracker.sendEventTracking(
                GlobalNavConstant.Analytics.EVENT_CLICK_APP_UPDATE,
                GlobalNavConstant.Analytics.CATEGORY_APP_UPDATE,
                GlobalNavConstant.Analytics.CLICK,
                isForceUpdate ?
                        GlobalNavConstant.Analytics.LABEL_FORCE_APP_UPDATE :
                        GlobalNavConstant.Analytics.LABEL_OPTIONAL_APP_UPDATE
        );
    }

    public void eventClickCancelAppUpdate(boolean isForceUpdate) {
        analyticTracker.sendEventTracking(
                GlobalNavConstant.Analytics.EVENT_CLICK_CANCEL_APP_UPDATE,
                GlobalNavConstant.Analytics.CATEGORY_APP_UPDATE,
                GlobalNavConstant.Analytics.CLICK,
                isForceUpdate ?
                        GlobalNavConstant.Analytics.LABEL_FORCE_CANCEL_APP_UPDATE :
                        GlobalNavConstant.Analytics.LABEL_OPTIONAL_CANCEL_APP_UPDATE
        );
    }

    public void trackFirstTime(Context context) {
        ((GlobalNavRouter) context.getApplicationContext()).sendAnalyticsFirstTime();
        LocalCacheHandler cache = new LocalCacheHandler(context, GlobalNavConstant.Cache.KEY_FIRST_TIME);
        cache.putBoolean(GlobalNavConstant.Cache.KEY_IS_FIRST_TIME, true);
        cache.applyEditor();
    }
}

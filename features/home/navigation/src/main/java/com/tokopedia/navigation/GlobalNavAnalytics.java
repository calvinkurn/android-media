package com.tokopedia.navigation;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

import static com.tokopedia.navigation.GlobalNavConstant.Analytics.*;

/**
 * Created by meta on 03/08/18.
 */
public class GlobalNavAnalytics {

    private AnalyticTracker analyticTracker;

    @Inject GlobalNavAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventBottomNavigation(String name) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                CLICK_HOMEPAGE,
                String.format("%s %s %s", HOME_PAGE, BOTTOM, NAV),
                String.format("%s %s %s", CLICK, name, NAV),
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
}

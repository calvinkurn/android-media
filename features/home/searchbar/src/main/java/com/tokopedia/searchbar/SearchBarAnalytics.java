package com.tokopedia.searchbar;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by meta on 04/08/18.
 */
public class SearchBarAnalytics {

    private AnalyticTracker analyticTracker;

    SearchBarAnalytics(Context context) {
        if (context == null)
            return;

        if (context.getApplicationContext() instanceof AbstractionRouter) {
            this.analyticTracker = ((SearchBarRouter)context.getApplicationContext())
                    .getAnalyticTracker();
        }
    }

    public void eventTrackingSqanQr() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                SearchBarConstant.CLICK_TOP_NAV,
                SearchBarConstant.TOP_NAV,
                String.format("%s %s", SearchBarConstant.CLICK,
                        SearchBarConstant.SCAN_QR),
                ""
        );
    }

    public void eventTrackingWishlist() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                SearchBarConstant.CLICK_TOP_NAV,
                SearchBarConstant.TOP_NAV,
                String.format("%s %s", SearchBarConstant.CLICK,
                        SearchBarConstant.WISHLIST),
                ""
        );
    }

    public void eventTrackingNotification() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                SearchBarConstant.CLICK_TOP_NAV,
                SearchBarConstant.TOP_NAV,
                String.format("%s %s", SearchBarConstant.CLICK,
                        SearchBarConstant.NOTIFICATION),
                ""
        );
    }
}

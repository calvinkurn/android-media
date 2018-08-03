package com.tokopedia.navigation;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * Created by meta on 03/08/18.
 */
public class GlobalNavAnalytics {

    private AnalyticTracker analyticTracker;

    @Inject
    public GlobalNavAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventWishlistClick() {
        analyticTracker.sendEventTracking(
                GlobalNavConstant.Analytics.CLICK_TOP_NAV,
                GlobalNavConstant.Analytics.TOP_NAV,
                String.format("%s %s", GlobalNavConstant.Analytics.CLICK,
                        GlobalNavConstant.Analytics.WISHLIST),
                ""
        );
    }
}

package com.tokopedia.tokocash.tracker;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by nabillasabbaha on 17/10/18.
 */
public class WalletAnalytics {

    private String GENERIC_EVENT = "clickSaldo";

    private AnalyticTracker analyticTracker;

    public WalletAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickActivationOvoHomepage() {
        analyticTracker.sendEventTracking(GENERIC_EVENT, Category.HOMEPAGE,
                Action.CLICK_HOME_ACTIVATION_OVO, "");
    }

    private static class Category {
        static String HOMEPAGE = "homepage";
    }

    private static class Action {
        static String CLICK_HOME_ACTIVATION_OVO = "click aktivasi ovo pada homepage";
    }
}

package com.tokopedia.saldodetails.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

public class SaldoEventAnalytics {

    private AnalyticTracker tracker;

    @Inject
    public SaldoEventAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void eventDepositTopUp() {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                SaldoEventConstant.Event.DEPOSIT,
                SaldoEventConstant.Category.DEPOSIT,
                SaldoEventConstant.Action.CLICK,
                SaldoEventConstant.EventLabel.TOPUP
        );
    }
}

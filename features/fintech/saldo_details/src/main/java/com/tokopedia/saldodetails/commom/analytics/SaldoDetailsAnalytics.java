package com.tokopedia.saldodetails.commom.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

public class SaldoDetailsAnalytics {

    private AnalyticTracker tracker;

    @Inject
    public SaldoDetailsAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void eventAnchorLabelClick(String eventAction) {
        if (tracker == null) {
            return;
        }
        tracker.sendEventTracking(
                SaldoDetailsConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                SaldoDetailsConstants.Category.SALDO_MAIN_SCREEN,
                String.format(SaldoDetailsConstants.Action.SALDO_ANCHOR_EVENT_ACTION, eventAction.toLowerCase()),
                SaldoDetailsConstants.EventLabel.SALDO_PAGE
        );
    }
}

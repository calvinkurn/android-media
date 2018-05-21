package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 21/05/18.
 */
public class CheckoutAnalyticsCartShipmentPage extends CheckoutAnalytics {
    @Inject
    public CheckoutAnalyticsCartShipmentPage(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickShipmentClickBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW,
                ""
        );
    }
}

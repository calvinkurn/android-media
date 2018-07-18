package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/07/18.
 */
public class OrderAnalyticsOrderTracking extends CheckoutAnalytics {
    @Inject
    public OrderAnalyticsOrderTracking(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventViewOrderOrderTrackingViewImpressionButtonLiveTracking() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.VIEW_ORDER,
                ConstantTransactionAnalytics.EventCategory.ORDER_TRACKING,
                ConstantTransactionAnalytics.EventAction.VIEW_IMPRESSION_BUTTON_LIVE_TRACKING,
                "");
    }

    public void eventClickOrderOrderTrackingClickButtonLiveTracking() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ORDER,
                ConstantTransactionAnalytics.EventCategory.ORDER_TRACKING,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_LIVE_TRACKING,
                "");
    }

}

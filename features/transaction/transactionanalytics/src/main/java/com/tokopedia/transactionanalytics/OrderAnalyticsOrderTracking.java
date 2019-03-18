package com.tokopedia.transactionanalytics;


import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;

/**
 * @author anggaprasetiyo on 18/07/18.
 */
public class OrderAnalyticsOrderTracking extends TransactionAnalytics {
    @Inject
    public OrderAnalyticsOrderTracking() {

    }

    public void eventViewOrderTrackingImpressionButtonLiveTracking() {
        sendEventCategoryAction(
                EventName.VIEW_ORDER,
                EventCategory.ORDER_TRACKING,
                EventAction.IMPRESSION_BUTTON_LIVE_TRACKING
        );
    }

    public void eventClickOrderTrackingClickButtonLiveTracking() {
        sendEventCategoryAction(
                EventName.CLICK_ORDER,
                EventCategory.ORDER_TRACKING,
                EventAction.CLICK_BUTTON_LIVE_TRACKING);
    }

}

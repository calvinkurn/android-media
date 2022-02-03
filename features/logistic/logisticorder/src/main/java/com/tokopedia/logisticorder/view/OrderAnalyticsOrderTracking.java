package com.tokopedia.logisticorder.view;


import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;

/**
 * @author anggaprasetiyo on 18/07/18.
 */
public class OrderAnalyticsOrderTracking extends TransactionAnalytics {
    @Inject
    public OrderAnalyticsOrderTracking() {

    }

    public void eventViewLabelTungguRetry(String countDownDuration, String orderId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_SOM,
                EventCategory.TRACK_SOM,
                EventAction.VIEW_TUNGGU_CARI_DRIVER,
                countDownDuration + " - " + orderId
        );
    }

    public void eventViewButtonCariDriver(String orderId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_SOM,
                EventCategory.TRACK_SOM,
                EventAction.VIEW_BUTTON_CARI_DRIVER,
                orderId
        );
    }

    public void eventClickButtonCariDriver(String orderId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_SOM,
                EventCategory.TRACK_SOM,
                EventAction.CLICK_BUTTON_CARI_DRIVER,
                orderId
        );
    }

}

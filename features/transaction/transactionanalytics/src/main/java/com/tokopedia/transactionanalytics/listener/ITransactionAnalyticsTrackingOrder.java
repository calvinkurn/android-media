package com.tokopedia.transactionanalytics.listener;

/**
 * @author anggaprasetiyo on 19/07/18.
 */
public interface ITransactionAnalyticsTrackingOrder {
    void sendAnalyticsOnViewTrackingRendered();

    void sendAnalyticsOnButtonLiveTrackingClicked();

}

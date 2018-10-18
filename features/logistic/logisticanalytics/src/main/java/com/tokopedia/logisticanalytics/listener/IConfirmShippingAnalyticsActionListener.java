package com.tokopedia.logisticanalytics.listener;

public interface IConfirmShippingAnalyticsActionListener {

    void sendAnalyticsOnClickButtonScan();

    void sendAnalyticsOnViewScanAwb();

    void sendAnalyticsOnClickButtonFinishWithSuccessResult();

    void sendAnalyticsOnClickButtonFinishWithFailedResult();

}

package com.tokopedia.logisticCommon.data.analytics.listener;

public interface IConfirmShippingAnalyticsActionListener {

    void sendAnalyticsOnClickButtonScan();

    void sendAnalyticsOnViewScanAwb();

    void sendAnalyticsOnClickButtonFinishWithSuccessResult();

    void sendAnalyticsOnClickButtonFinishWithFailedResult();

}

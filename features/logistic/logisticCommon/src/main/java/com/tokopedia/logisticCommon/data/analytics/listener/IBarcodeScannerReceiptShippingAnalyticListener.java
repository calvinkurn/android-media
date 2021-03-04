package com.tokopedia.logisticCommon.data.analytics.listener;

public interface IBarcodeScannerReceiptShippingAnalyticListener {

    void sendAnalyticsOnImpressionBarcodeScanner();

    void sendAnalyticsOnCloseBarcodeScanner();
}

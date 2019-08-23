package com.tokopedia.logisticdata.data.analytics.listener;

public interface IBarcodeScannerReceiptShippingAnalyticListener {

    void sendAnalyticsOnImpressionBarcodeScanner();

    void sendAnalyticsOnCloseBarcodeScanner();
}

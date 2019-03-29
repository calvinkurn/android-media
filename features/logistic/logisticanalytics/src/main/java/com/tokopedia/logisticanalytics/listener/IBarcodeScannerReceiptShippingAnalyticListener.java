package com.tokopedia.logisticanalytics.listener;

public interface IBarcodeScannerReceiptShippingAnalyticListener {

    void sendAnalyticsOnImpressionBarcodeScanner();

    void sendAnalyticsOnCloseBarcodeScanner();
}

package com.tokopedia.tracking.view.shipping_confirmation.view.barcodescanner;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.logisticdata.data.analytics.SalesShippingAnalytics;
import com.tokopedia.logisticdata.data.analytics.listener.IBarcodeScannerReceiptShippingAnalyticListener;
import com.tokopedia.tracking.R;

public class ReceiptShipmentBarcodeScannerActivity extends CaptureActivity
        implements IBarcodeScannerReceiptShippingAnalyticListener {

    private SalesShippingAnalytics salesShippingAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            salesShippingAnalytics = new SalesShippingAnalytics();
        sendAnalyticsOnImpressionBarcodeScanner();

    }

    @Override
    public void onBackPressed() {
        sendAnalyticsOnCloseBarcodeScanner();
        super.onBackPressed();
    }

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_receipt_shipment_barcode_scanner);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    public void sendAnalyticsOnImpressionBarcodeScanner() {
        salesShippingAnalytics.eventViewShippingSalesShippingImpressionScanAwbPage();
    }

    @Override
    public void sendAnalyticsOnCloseBarcodeScanner() {
        salesShippingAnalytics.eventClickShippingSalesShippingClickExitScanAwb();
    }
}
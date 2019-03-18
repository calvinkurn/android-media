package com.tokopedia.logisticinputreceiptshipment.view.barcodescanner;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.logisticanalytics.SalesShippingAnalytics;
import com.tokopedia.logisticanalytics.listener.IBarcodeScannerReceiptShippingAnalyticListener;
import com.tokopedia.logisticinputreceiptshipment.R;

public class ReceiptShipmentBarcodeScannerActivity extends CaptureActivity
        implements IBarcodeScannerReceiptShippingAnalyticListener {

    private SalesShippingAnalytics salesShippingAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplicationContext() instanceof AbstractionRouter) {
            salesShippingAnalytics = new SalesShippingAnalytics();
        }
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
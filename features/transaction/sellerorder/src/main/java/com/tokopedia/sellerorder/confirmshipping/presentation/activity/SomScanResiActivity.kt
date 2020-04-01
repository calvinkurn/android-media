package com.tokopedia.sellerorder.confirmshipping.presentation.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_SCAN_BARCODE

/**
 * Created by fwidjaja on 2019-11-16.
 */
class SomScanResiActivity: CaptureActivity() {

    override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.activity_barcode_scanner)
        return findViewById<View>(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
    }
}
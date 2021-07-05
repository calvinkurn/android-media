package com.tokopedia.sellerorder.confirmshipping.presentation.activity

import android.view.View
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.tokopedia.sellerorder.R

/**
 * Created by fwidjaja on 2019-11-16.
 */
class SomScanResiActivity: CaptureActivity() {

    override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.activity_barcode_scanner)
        return findViewById<View>(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
    }
}
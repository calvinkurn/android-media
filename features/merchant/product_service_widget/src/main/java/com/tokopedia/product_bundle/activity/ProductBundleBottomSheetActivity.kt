package com.tokopedia.product_bundle.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.product_service_widget.R

class ProductBundleBottomSheetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_bundle_bottomsheet)
        setOrientation()
        showBottomSheet()
    }

    private fun showBottomSheet() {
        val bottomSheet = ProductBundleBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            finish()
        }
        bottomSheet.show(supportFragmentManager)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
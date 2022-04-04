package com.tokopedia.product_bundle.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.product_service_widget.R

class ProductBundleBottomSheetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_bundle_bottomsheet)
        showBottomSheet()
    }

    private fun showBottomSheet() {
        val bottomSheet = ProductBundleBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            finish()
        }
        bottomSheet.show(supportFragmentManager)
    }
}
package com.tokopedia.product_bundle.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.product_service_widget.R

class ProductBundleBottomSheetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_bundle_bottomsheet)
        setupStatusBar()
        showBottomSheet()
    }

    private fun showBottomSheet() {
        val bottomSheet = ProductBundleBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            finish()
        }
        bottomSheet.show(supportFragmentManager)
    }

    private fun setupStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background)
    }
}
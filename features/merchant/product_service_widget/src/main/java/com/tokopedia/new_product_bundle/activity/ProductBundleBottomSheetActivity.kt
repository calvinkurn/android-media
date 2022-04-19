package com.tokopedia.new_product_bundle.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.new_product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.oldproductbundle.activity.ProductBundleActivity
import com.tokopedia.product_service_widget.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class ProductBundleBottomSheetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getRemoteConfigEnableOldBundleSelectionPage()) {
            directToOldActivity()
        }

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

    private fun directToOldActivity() {
        val newIntent = Intent(this, ProductBundleActivity::class.java)
        newIntent.data = intent.data
        startActivity(newIntent)
        finish()
    }

    private fun getRemoteConfigEnableOldBundleSelectionPage(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(this)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_OLD_BUNDLE_SELECTION_PAGE, false)
    }
}
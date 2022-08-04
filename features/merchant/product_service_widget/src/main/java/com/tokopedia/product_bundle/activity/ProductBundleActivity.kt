package com.tokopedia.product_bundle.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.oldproductbundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.common.data.mapper.ViewType.BOTTOMSHEET
import com.tokopedia.product_bundle.common.data.mapper.ViewType.PAGE
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.fragment.EntrypointFragment.Companion.newInstance
import com.tokopedia.product_service_widget.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class ProductBundleActivity : BaseSimpleActivity() {

    private var entrypointFragment: EntrypointFragment = EntrypointFragment()

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getLayoutRes() = R.layout.activity_product_bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        checkRemoteConfig()
        setOrientation()

        intent?.data?.let { data ->
            when(ProductBundleApplinkMapper.getViewType(data)) {
                BOTTOMSHEET.name -> directToBundleSelectionBottomSheet(savedInstanceState)
                PAGE.name -> directToBundleSelectionPage(savedInstanceState, data)
                else -> super.onCreate(savedInstanceState)
            }
        }
    }

    override fun getNewFragment(): Fragment = entrypointFragment

    private fun directToBundleSelectionBottomSheet(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bottomSheet = ProductBundleBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            finish()
        }

        bottomSheet.show(supportFragmentManager)
    }

    private fun directToBundleSelectionPage(savedInstanceState: Bundle?, data: Uri) {
        val bundleId = ProductBundleApplinkMapper.getBundleIdFromUri(data)
        val selectedProductIds = ProductBundleApplinkMapper.getSelectedProductIdsFromUri(data)
        val parentProductId = ProductBundleApplinkMapper.getProductIdFromUri(data, data.pathSegments.orEmpty())
        val source = ProductBundleApplinkMapper.getPageSourceFromUri(data)
        val warehouseId = ProductBundleApplinkMapper.getWarehouseIdFromUri(data)

        entrypointFragment = newInstance(
            bundleId = bundleId,
            selectedProductsId = ArrayList(selectedProductIds),
            source = source,
            parentProductId = parentProductId,
            warehouseId = warehouseId
        )

        setTheme(com.tokopedia.abstraction.R.style.Theme_WhiteUnify)
        super.onCreate(savedInstanceState)
    }

    private fun checkRemoteConfig() {
        if (getRemoteConfigEnableOldBundleSelectionPage()) {
            val newIntent = Intent(this, ProductBundleActivity::class.java)
            newIntent.data = intent.data
            startActivity(newIntent)
            finish()
        }
    }

    private fun getRemoteConfigEnableOldBundleSelectionPage(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(this)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_OLD_BUNDLE_SELECTION_PAGE, false)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    fun getEntrypointFragment() = entrypointFragment
}
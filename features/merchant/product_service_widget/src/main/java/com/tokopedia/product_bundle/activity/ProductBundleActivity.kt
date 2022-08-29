package com.tokopedia.product_bundle.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.common.data.mapper.ViewType.BOTTOMSHEET
import com.tokopedia.product_bundle.common.data.mapper.ViewType.PAGE
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.fragment.EntrypointFragment.Companion.newInstance
import com.tokopedia.product_service_widget.R

class ProductBundleActivity : BaseSimpleActivity() {

    private var _entrypointFragment: EntrypointFragment = EntrypointFragment()
    val entrypointFragment get() = _entrypointFragment

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getLayoutRes() = R.layout.activity_product_bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        setOrientation()

        intent?.data?.let { data ->
            when(ProductBundleApplinkMapper.getViewType(data)) {
                BOTTOMSHEET.name -> directToBundleSelectionBottomSheet(savedInstanceState)
                PAGE.name -> directToBundleSelectionPage(savedInstanceState, data)
                else -> super.onCreate(savedInstanceState)
            }
        }
    }

    override fun getNewFragment(): Fragment = _entrypointFragment

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

        _entrypointFragment = newInstance(
            bundleId = bundleId,
            selectedProductsId = ArrayList(selectedProductIds),
            source = source,
            parentProductId = parentProductId,
            warehouseId = warehouseId
        )

        setTheme(com.tokopedia.abstraction.R.style.Theme_WhiteUnify)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
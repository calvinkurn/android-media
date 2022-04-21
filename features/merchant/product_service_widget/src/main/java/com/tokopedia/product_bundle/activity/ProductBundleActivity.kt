package com.tokopedia.product_bundle.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.oldproductbundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.fragment.EntrypointFragment.Companion.newInstance
import com.tokopedia.product_service_widget.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class ProductBundleActivity : BaseSimpleActivity() {

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes() = R.layout.activity_product_bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getRemoteConfigEnableOldBundleSelectionPage()) {
            directToOldActivity()
        }
    }

    override fun getNewFragment(): Fragment {
        return intent?.data?.let {
            val source = ProductBundleApplinkMapper.getPageSourceFromUri(it)
            directToBottomSheetIfMiniCartSource(source, it)

            val bundleId = ProductBundleApplinkMapper.getBundleIdFromUri(it)
            val selectedProductIds = ProductBundleApplinkMapper.getSelectedProductIdsFromUri(it)
            val parentProductId = ProductBundleApplinkMapper.getProductIdFromUri(it, it.pathSegments.orEmpty())

            newInstance(
                bundleId = bundleId,
                selectedProductsId = ArrayList(selectedProductIds),
                source = source,
                parentProductId = parentProductId
            )
        } ?: EntrypointFragment()
    }

    private fun directToBottomSheetIfMiniCartSource(source: String, data: Uri) {
        if (source == ProductBundleApplinkMapper.MINICART) {
            val newIntent = Intent(this, ProductBundleBottomSheetActivity::class.java)
            newIntent.data = data
            startActivity(newIntent)
            finish()
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
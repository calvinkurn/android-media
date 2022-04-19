package com.tokopedia.product_bundle.activity

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.fragment.EntrypointFragment.Companion.newInstance
import com.tokopedia.product_service_widget.R

class ProductBundleActivity : BaseSimpleActivity() {

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getLayoutRes() = R.layout.activity_product_bundle

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
}
package com.tokopedia.product_bundle.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductBundleBottomSheet : BottomSheetUnify(){
    companion object {
        private val TAG = ProductBundleBottomSheet::class.simpleName

        fun newInstance(): ProductBundleBottomSheet {
            return ProductBundleBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottomsheet_product_bundle, container, false)
        initView(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entryPoint = createEntryPoint()
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.parent_view, entryPoint)
        ft.commit()
    }

    private fun createEntryPoint(): EntrypointFragment {
        return activity?.intent?.data?.let {
            val source = ProductBundleApplinkMapper.getPageSourceFromUri(it)
            val bundleId = ProductBundleApplinkMapper.getBundleIdFromUri(it)
            val selectedProductIds = ProductBundleApplinkMapper.getSelectedProductIdsFromUri(it)
            val parentProductId = ProductBundleApplinkMapper.getProductIdFromUri(it, it.pathSegments.orEmpty())
            val warehouseId = ProductBundleApplinkMapper.getWarehouseIdFromUri(it)
            setTitle(getTitle(source))

            EntrypointFragment.newInstance(
                bundleId = bundleId,
                selectedProductsId = ArrayList(selectedProductIds),
                source = source,
                parentProductId = parentProductId,
                warehouseId = warehouseId
            )

        } ?: EntrypointFragment()
    }

    private fun initView(view: View) {
        setChild(view)
        isFullpage = true
        clearContentPadding = true
    }

    private fun getTitle(pageSource: String): String {
        return when (pageSource) {
            ProductBundleConstants.PAGE_SOURCE_MINI_CART -> getString(R.string.product_bundle_bottomsheet_title)
            else -> getString(R.string.product_bundle_page_title)
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}
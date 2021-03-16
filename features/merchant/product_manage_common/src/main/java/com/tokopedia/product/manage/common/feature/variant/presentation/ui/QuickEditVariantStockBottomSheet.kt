package com.tokopedia.product.manage.common.feature.variant.presentation.ui

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerData
import com.tokopedia.product.manage.common.feature.variant.adapter.ProductVariantAdapter
import com.tokopedia.product.manage.common.feature.variant.adapter.factory.ProductVariantStockAdapterFactoryImpl
import com.tokopedia.product.manage.common.feature.variant.adapter.viewholder.ProductVariantStockViewHolder
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class QuickEditVariantStockBottomSheet(
    private val onSaveVariantsStock: (EditVariantResult) -> Unit = {}
): QuickEditVariantBottomSheet(), ProductVariantStockViewHolder.ProductVariantStockListener {

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"
        val TAG: String = QuickEditVariantStockBottomSheet::class.java.simpleName

        fun createInstance(
            productId: String,
            onSaveVariantsStock: (EditVariantResult) -> Unit
        ): QuickEditVariantStockBottomSheet {
            return QuickEditVariantStockBottomSheet(onSaveVariantsStock).apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_PRODUCT_ID, productId)
                arguments = bundle
            }
        }
    }

    private val variantStockAdapter by lazy {
        ProductVariantAdapter(ProductVariantStockAdapterFactoryImpl(this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewState()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getTitle(): String {
        return context?.getString(R.string.product_manage_quick_edit_stock_title).orEmpty()
    }

    override fun createAdapter(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return variantStockAdapter
    }

    override fun onSaveButtonClicked(result: EditVariantResult) {
        ProductManageTracking.eventClickEditStockVariantSave()
        onSaveVariantsStock(result)
        dismiss()
    }

    override fun onStockBtnClicked() {
        viewModel.getTickerList()
    }

    override fun onStockChanged(variantId: String, stock: Int) {
        variantStockAdapter.updateVariantStock(variantId, stock)
        viewModel.setVariantStock(variantId, stock)
    }

    override fun onStatusChanged(variantId: String, status: ProductStatus) {
        ProductManageTracking.eventClickStatusToggleVariant(status)
        variantStockAdapter.updateVariantStatus(variantId, status)
        viewModel.setVariantStatus(variantId, status)
    }

    private fun observeViewState() {
        observe(viewModel.tickerList) { data ->
            if(data.isNotEmpty()) {
                val tickerList = mapToTickerData(context, data)
                variantStockAdapter.showTicker(tickerList)
            } else {
                variantStockAdapter.hideTicker()
            }
        }
        observe(viewModel.showStockInfo) { showStockInfo ->
            if(showStockInfo) {
                variantStockAdapter.showStockInfo()
            } else {
                variantStockAdapter.hideStockInfo()
            }
        }
    }
}
package com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.ProductVariantAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantStockAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantStockViewHolder.ProductVariantStockListener
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class QuickEditVariantStockBottomSheet(
    private val onSaveVariantsStock: (EditVariantResult) -> Unit
): QuickEditVariantBottomSheet(), ProductVariantStockListener {

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

    override fun getTitle(): String {
        return context?.getString(R.string.product_manage_quick_edit_stock_title).orEmpty()
    }

    override fun createAdapter(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return ProductVariantAdapter(ProductVariantStockAdapterFactoryImpl(this))
    }

    override fun onSaveButtonClicked(result: EditVariantResult) {
        onSaveVariantsStock(result)
        dismiss()
    }

    override fun onStockChanged(variantId: String, stock: Int) {
        viewModel.setVariantStock(variantId, stock)
    }

    override fun onStatusChanged(variantId: String, status: ProductStatus) {
        viewModel.setVariantStatus(variantId, status)
    }
}
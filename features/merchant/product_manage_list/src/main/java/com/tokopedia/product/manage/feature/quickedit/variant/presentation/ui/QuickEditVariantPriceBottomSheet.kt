package com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.ProductVariantAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantPriceViewHolder.*
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.EditVariantResult

class QuickEditVariantPriceBottomSheet(
    private val onSaveVariantsPrice: (EditVariantResult) -> Unit
): QuickEditVariantBottomSheet(), ProductVariantListener {

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"
        val TAG: String = QuickEditVariantPriceBottomSheet::class.java.simpleName

        fun createInstance(
            productId: String,
            onSaveVariantsPrice: (EditVariantResult) -> Unit
        ): QuickEditVariantPriceBottomSheet {
            return QuickEditVariantPriceBottomSheet(onSaveVariantsPrice).apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_PRODUCT_ID, productId)
                arguments = bundle
            }
        }
    }

    override fun getTitle(): String {
        return context?.getString(R.string.product_manage_menu_set_price).orEmpty()
    }

    override fun createAdapter(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return ProductVariantAdapter(ProductVariantAdapterFactoryImpl(this))
    }

    override fun onPriceChanged(variantId: String, price: Int) {
        viewModel.updateVariantPrice(variantId, price)
    }

    override fun onSaveButtonClicked(result: EditVariantResult) {
        onSaveVariantsPrice(result)
        dismiss()
    }
}
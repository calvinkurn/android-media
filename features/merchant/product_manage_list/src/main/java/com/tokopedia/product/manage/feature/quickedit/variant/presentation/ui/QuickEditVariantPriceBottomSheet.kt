package com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_PRICE
import com.tokopedia.product.manage.common.feature.variant.presentation.ui.QuickEditVariantBottomSheet
import com.tokopedia.product.manage.common.feature.variant.adapter.ProductVariantAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantPriceAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantPriceViewHolder.ProductVariantListener
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult

class QuickEditVariantPriceBottomSheet(
    private val onSaveVariantsPrice: (EditVariantResult) -> Unit = {}
): QuickEditVariantBottomSheet(), ProductVariantListener {

    companion object {
        val TAG: String = QuickEditVariantPriceBottomSheet::class.java.simpleName

        fun createInstance(
            productId: String,
            isBundling: Boolean = false,
            isMultiLocationShop: Boolean = false,
            onSaveVariantsPrice: (EditVariantResult) -> Unit
        ): QuickEditVariantPriceBottomSheet {
            return QuickEditVariantPriceBottomSheet(onSaveVariantsPrice).apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_PRODUCT_ID, productId)
                bundle.putBoolean(EXTRA_IS_BUNDLING, isBundling)
                bundle.putBoolean(EXTRA_IS_MULTILOCATION, isMultiLocationShop)
                arguments = bundle
            }
        }
    }

    override fun getTitle(): String {
        return context?.getString(R.string.product_manage_menu_set_price).orEmpty()
    }

    override fun createAdapter(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return ProductVariantAdapter(ProductVariantPriceAdapterFactoryImpl(this))
    }

    override fun onPriceChanged(variantId: String, price: Double) {
        viewModel.setVariantPrice(variantId, price)
    }

    override fun onSaveButtonClicked(result: EditVariantResult) {
        if(isVariantsPriceValid(result)) {
            ProductManageTracking.eventClickEditPriceVariantSave()
            onSaveVariantsPrice(result)
            dismiss()
        }
    }

    private fun isVariantsPriceValid(result: EditVariantResult): Boolean {
        result.variants.forEach {
            if(it.price < MINIMUM_PRICE.toDouble()) {
                return false
            }
        }
        return true
    }
}
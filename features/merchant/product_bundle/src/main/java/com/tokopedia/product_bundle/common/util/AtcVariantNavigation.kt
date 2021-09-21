package com.tokopedia.product_bundle.common.util

import androidx.fragment.app.Fragment
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleItem

object AtcVariantNavigation {

    fun showVariantBottomSheet(fragment: Fragment, bundleItem: BundleItem) {
        val productVariant = AtcVariantMapper.mapToProductVariant(bundleItem)
        showVariantBottomSheet(fragment, productVariant)
    }

    fun showVariantBottomSheet(fragment: Fragment, productVariant: ProductVariant) {
        val saveButtonText = fragment.getString(R.string.action_save)
        val cartRedirections = AtcVariantHelper.generateSimpanCartRedirection(productVariant, saveButtonText)
            ?: emptyMap()

        AtcVariantHelper.pdpToAtcVariant(
            context = fragment.requireContext(),
            productInfoP1 = DynamicProductInfoP1(),
            warehouseId = "",
            pdpSession = "",
            isTokoNow = false,
            isShopOwner = false,
            productVariant = productVariant,
            warehouseResponse = mapOf("" to WarehouseInfo()),
            cartRedirection = cartRedirections,
            miniCart = emptyMap(),
            alternateCopy = emptyList(),
            boData = null,
            rates = null,
            restrictionData = null,
            pageSource = AtcVariantHelper.BUNDLING_PAGESOURCE
        ) { intent, resultCode ->
            when (resultCode) {
                AtcVariantHelper.ATC_VARIANT_RESULT_CODE -> {
                    fragment.startActivityForResult(intent, resultCode)
                }
            }
        }
    }
}
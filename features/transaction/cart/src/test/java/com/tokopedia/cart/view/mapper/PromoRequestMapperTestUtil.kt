package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem

object PromoRequestMapperTestUtil {
    
    fun mapCartProductModelToPromoProductDetailsItem(
        productModels: List<CartItemHolderData>
    ): List<ProductDetailsItem> {
        val tmpProductDetails = mutableListOf<ProductDetailsItem>()
        productModels.forEach { cartItemHolderData ->
            if (cartItemHolderData.isSelected) {
                val productDetailsItem = ProductDetailsItem(
                    productId = cartItemHolderData.productId.toLongOrZero(),
                    quantity = cartItemHolderData.quantity,
                    bundleId = cartItemHolderData.bundleId.toLongOrZero()
                )
                tmpProductDetails.add(productDetailsItem)
            }
        }
        return tmpProductDetails
    }
    
    fun mapToCouponListProductDetailsItem(
        productModels: List<CartItemHolderData>
    ): List<ProductDetail> {
        val tmpProductDetails = mutableListOf<ProductDetail>()
        productModels.forEach { cartItemHolderData ->
            if (cartItemHolderData.isSelected) {
                val productDetailsItem = ProductDetail(
                    productId = cartItemHolderData.productId.toLongOrZero(),
                    quantity = cartItemHolderData.quantity,
                    bundleId = cartItemHolderData.bundleId.toLongOrZero()
                )
                tmpProductDetails.add(productDetailsItem)
            }
        }
        return tmpProductDetails
    }
}

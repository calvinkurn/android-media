package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem

object PromoRequestMapperTestHelper {

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

    fun getFirstCartOrder(): MutableList<CartItemHolderData> {
        return mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
    }

    fun getSecondCartOrder(): MutableList<CartItemHolderData> {
        return mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
    }
}

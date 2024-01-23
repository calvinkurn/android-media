package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.request.CartShopGroupTickerGroupMetadata
import com.tokopedia.cart.view.uimodel.CartGroupHolderData

object CartShopGroupTickerGroupMetadataRequestMapper {
    fun generateGroupMetadata(cartGroupHolderData: CartGroupHolderData): CartShopGroupTickerGroupMetadata {
        val listGroupProductData = arrayListOf<CartShopGroupTickerGroupMetadata.OrderData.GroupProduct>()
        cartGroupHolderData.productUiModelList.map {
            listGroupProductData.add(
                CartShopGroupTickerGroupMetadata.OrderData.GroupProduct(
                    groupQty = it.bundleQuantity

                )
            )
        }
        return CartShopGroupTickerGroupMetadata(
            listOrderData = arrayListOf(
                CartShopGroupTickerGroupMetadata.OrderData(
                    uniqueId = cartGroupHolderData.productUiModelList.first().cartStringOrder,
                    shop = CartShopGroupTickerGroupMetadata.OrderData.Shop(shopId = cartGroupHolderData.shop.shopId),
                    warehouseId = cartGroupHolderData.warehouseId.toString(),
                    listGroupProduct = arrayListOf()
                )
            )
        )
    }
}

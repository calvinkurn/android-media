package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.request.CartShopGroupTickerGroupMetadata
import com.tokopedia.cart.view.uimodel.CartGroupHolderData

object CartShopGroupTickerGroupMetadataRequestMapper {
    fun generateGroupMetadata(cartGroupHolderData: CartGroupHolderData): CartShopGroupTickerGroupMetadata {
        val listOrderData = arrayListOf<CartShopGroupTickerGroupMetadata.OrderData>()
        val listGroupProductData = arrayListOf<CartShopGroupTickerGroupMetadata.OrderData.GroupProduct>()
        cartGroupHolderData.productUiModelList.groupBy { it.cartString }.map { groupedByCartString ->
            if (groupedByCartString.value.first().bundleGroupId.isNotEmpty()) {
                groupedByCartString.value.groupBy { it.bundleGroupId }.map { groupedByBundleGroupId ->
                    val listProductBundle = arrayListOf<CartShopGroupTickerGroupMetadata.OrderData.GroupProduct.Product>()
                    groupedByBundleGroupId.value.forEach { cartItem ->
                        listProductBundle.add(
                            CartShopGroupTickerGroupMetadata.OrderData.GroupProduct.Product(
                                uniqueId = cartItem.cartId,
                                productId = cartItem.productId,
                                quantity = cartItem.quantity,
                                productMetadata = cartItem.productMetadata
                            )
                        )
                    }

                    listGroupProductData.add(
                        CartShopGroupTickerGroupMetadata.OrderData.GroupProduct(
                            groupQty = groupedByBundleGroupId.value.first().bundleQuantity,
                            listProduct = listProductBundle
                        )
                    )
                }
            } else {
                groupedByCartString.value.groupBy { it.identifier }.map { groupedByIdentifier ->
                    val listProduct = arrayListOf<CartShopGroupTickerGroupMetadata.OrderData.GroupProduct.Product>()
                    groupedByIdentifier.value.forEach { cartItem ->
                        listProduct.add(
                            CartShopGroupTickerGroupMetadata.OrderData.GroupProduct.Product(
                                uniqueId = cartItem.cartId,
                                productId = cartItem.productId,
                                quantity = cartItem.quantity
                            )
                        )
                    }

                    listGroupProductData.add(
                        CartShopGroupTickerGroupMetadata.OrderData.GroupProduct(
                            groupQty = 1,
                            listProduct = listProduct
                        )
                    )
                }
            }

            listOrderData.add(
                CartShopGroupTickerGroupMetadata.OrderData(
                    uniqueId = groupedByCartString.key,
                    shop = CartShopGroupTickerGroupMetadata.OrderData.Shop(shopId = groupedByCartString.value[0].shopHolderData.shopId),
                    warehouseId = groupedByCartString.value[0].warehouseId,
                    listGroupProduct = listGroupProductData,
                    orderMetadata = groupedByCartString.value[0].orderMetadata
                )
            )
        }
        return CartShopGroupTickerGroupMetadata(
            listOrderData = listOrderData
        )
    }
}

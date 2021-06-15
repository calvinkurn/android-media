package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.minicart.common.data.response.minicartlist.Data
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import javax.inject.Inject

class MiniCartSimplifiedMapper @Inject constructor() {

    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): MiniCartSimplifiedData {
        return MiniCartSimplifiedData().apply {
            miniCartItems = mapMiniCartListData(miniCartData)
            isShowMiniCartWidget = miniCartItems.isNotEmpty()
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
        }
    }

    private fun mapMiniCartWidgetData(miniCartData: MiniCartData): MiniCartWidgetData {
        return MiniCartWidgetData().apply {
            totalProductCount = miniCartData.data.totalProductCount
            totalProductPrice = miniCartData.data.totalProductPrice
            totalProductError = miniCartData.data.totalProductError
            containsOnlyUnavailableItems = miniCartData.data.availableSection.availableGroup.isEmpty() && miniCartData.data.unavailableSection.isNotEmpty()
            unavailableItemsCount = miniCartData.data.totalProductError
        }
    }

    private fun mapMiniCartListData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()
        miniCartSimplifiedDataList.addAll(getAvailableData(miniCartData))
        miniCartSimplifiedDataList.addAll(getUnavailableData(miniCartData))
        return miniCartSimplifiedDataList
    }

    private fun getAvailableData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()

        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                miniCartSimplifiedDataList.add(
                        MiniCartItem().apply {
                            isError = false
                            cartId = cartDetail.cartId
                            productId = cartDetail.product.productId
                            productParentId = cartDetail.product.parentId
                            quantity = cartDetail.product.productQuantity
                            notes = cartDetail.product.productNotes
                            campaignId = cartDetail.product.campaignId
                            attribution = cartDetail.product.productTrackerData.attribution
                            productWeight = cartDetail.product.productWeight
                            productSlashPriceLabel = cartDetail.product.slashPriceLabel
                            warehouseId = cartDetail.product.warehouseId
                            shopId = availableGroup.shop.shopId
                            shopName = availableGroup.shop.shopName
                            shopType = availableGroup.shop.shopTypeInfo.titleFmt
                            categoryId = cartDetail.product.categoryId
                            freeShippingType =
                                    if (availableGroup.shipmentInformation.freeShippingExtra.eligible) "bebas ongkir extra"
                                    else if (availableGroup.shipmentInformation.freeShipping.eligible) "bebas ongkir"
                                    else ""
                            category = cartDetail.product.category
                            productName = cartDetail.product.productName
                            productVariantName = cartDetail.product.variantDescriptionDetail.variantName.joinToString(", ")
                            productPrice = cartDetail.product.productPrice
                            productQty = cartDetail.product.productQuantity
                        }
                )
            }
        }

        return miniCartSimplifiedDataList
    }

    private fun getUnavailableData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()

        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    miniCartSimplifiedDataList.add(
                            MiniCartItem().apply {
                                isError = true
                                cartId = cartDetail.cartId
                                productId = cartDetail.product.productId
                                productParentId = cartDetail.product.parentId
                                quantity = cartDetail.product.productQuantity
                                notes = cartDetail.product.productNotes
                            }
                    )
                }
            }
        }

        return miniCartSimplifiedDataList
    }

}
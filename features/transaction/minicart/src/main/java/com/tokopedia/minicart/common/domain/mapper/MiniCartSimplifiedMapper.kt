package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.minicart.common.data.response.minicartlist.BeliButtonConfig
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItem2
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData2
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import javax.inject.Inject
import kotlin.math.min

class MiniCartSimplifiedMapper @Inject constructor() {

//    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): MiniCartSimplifiedData {
//        return MiniCartSimplifiedData().apply {
//            miniCartItems = mapMiniCartListData(miniCartData)
//            isShowMiniCartWidget = miniCartItems.isNotEmpty()
//            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
//        }
//    }

    fun mapMiniCartSimplifiedData2(miniCartData: MiniCartData): MiniCartSimplifiedData2 {
        return MiniCartSimplifiedData2().apply {
            miniCartItems = mapMiniCartListData2(miniCartData)
            isShowMiniCartWidget = miniCartItems.isNotEmpty()
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
        }
    }

    private fun mapMiniCartWidgetData(miniCartData: MiniCartData): MiniCartWidgetData {
        var totalQty = 0
        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                totalQty += cartDetail.product.productQuantity
            }
        }
        return MiniCartWidgetData().apply {
            totalProductCount = totalQty
            totalProductPrice = miniCartData.data.totalProductPrice
            totalProductError = miniCartData.data.totalProductError
            containsOnlyUnavailableItems = miniCartData.data.availableSection.availableGroup.isEmpty() && miniCartData.data.unavailableSection.isNotEmpty()
            unavailableItemsCount = miniCartData.data.totalProductError
            isOCCFlow = miniCartData.data.beliButtonConfig.buttonType == BeliButtonConfig.BUTTON_TYPE_OCC
            buttonBuyWording = miniCartData.data.beliButtonConfig.buttonWording
        }
    }

    private fun mapMiniCartListData(miniCartData: MiniCartData): List<MiniCartItem> {
        val miniCartSimplifiedDataList = mutableListOf<MiniCartItem>()
        miniCartSimplifiedDataList.addAll(getAvailableData(miniCartData))
        miniCartSimplifiedDataList.addAll(getUnavailableData(miniCartData))
        return miniCartSimplifiedDataList
    }

    private fun mapMiniCartListData2(miniCartData: MiniCartData): Map<MiniCartItemKey, MiniCartItem2> {
        val miniCartSimplifiedDataList = hashMapOf<MiniCartItemKey, MiniCartItem2>()
        miniCartSimplifiedDataList.putAll(getAvailableData2(miniCartData))
        getUnavailableData2(miniCartData, miniCartSimplifiedDataList)
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
                            quantity = if (cartDetail.product.productSwitchInvenage == 0) {
                                cartDetail.product.productQuantity
                            } else {
                                min(cartDetail.product.productQuantity, cartDetail.product.productInvenageValue)
                            }
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

    private fun getAvailableData2(miniCartData: MiniCartData): Map<MiniCartItemKey, MiniCartItem2> {
        val miniCartSimplifiedDataList = hashMapOf<MiniCartItemKey, MiniCartItem2>()

        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                val item = MiniCartItem2.MiniCartItemProduct().apply {
                    isError = false
                    cartId = cartDetail.cartId
                    productId = cartDetail.product.productId
                    productParentId = cartDetail.product.parentId
                    quantity = if (cartDetail.product.productSwitchInvenage == 0) {
                        cartDetail.product.productQuantity
                    } else {
                        min(cartDetail.product.productQuantity, cartDetail.product.productInvenageValue)
                    }
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
                }
                val key = MiniCartItemKey(cartDetail.product.productId)
                miniCartSimplifiedDataList[key] = item

                if (item.productParentId.isNotBlankOrZero()) {
                    val parentKey = MiniCartItemKey(cartDetail.product.parentId, type = MiniCartItemType.PARENT)
                    if (!miniCartSimplifiedDataList.contains(parentKey)) {
                        miniCartSimplifiedDataList[parentKey] = MiniCartItem2.MiniCartItemParentProduct(
                                parentId = cartDetail.product.parentId,
                                totalQuantity = item.quantity,
                                products = hashMapOf(key to item)
                        )
                    } else {
                        val currentParentItem = miniCartSimplifiedDataList[parentKey] as MiniCartItem2.MiniCartItemParentProduct
                        val products = HashMap(currentParentItem.products)
                        products[key] = item
                        val totalQuantity = currentParentItem.totalQuantity + item.quantity
                        miniCartSimplifiedDataList[parentKey] = MiniCartItem2.MiniCartItemParentProduct(
                                cartDetail.product.parentId, totalQuantity, products
                        )
                    }
                }
            }
        }

        return miniCartSimplifiedDataList
    }

    private fun getUnavailableData2(miniCartData: MiniCartData, miniCartSimplifiedDataList: MutableMap<MiniCartItemKey, MiniCartItem2>): Map<MiniCartItemKey, MiniCartItem2> {
        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    val item = MiniCartItem2.MiniCartItemProduct().apply {
                        isError = true
                        cartId = cartDetail.cartId
                        productId = cartDetail.product.productId
                        productParentId = cartDetail.product.parentId
                        quantity = cartDetail.product.productQuantity
                        notes = cartDetail.product.productNotes
                    }
                    val key = MiniCartItemKey(cartDetail.product.productId)
                    if (!miniCartSimplifiedDataList.contains(key)) {
                        miniCartSimplifiedDataList[key] = item
                    }
                    if (item.productParentId.isNotBlankOrZero()) {
                        val parentKey = MiniCartItemKey(cartDetail.product.parentId, type = MiniCartItemType.PARENT)
                        if (!miniCartSimplifiedDataList.contains(parentKey)) {
                            miniCartSimplifiedDataList[parentKey] = MiniCartItem2.MiniCartItemParentProduct(
                                    parentId = cartDetail.product.parentId,
                                    totalQuantity = item.quantity,
                                    products = hashMapOf(key to item)
                            )
                        } else {
                            val currentParentItem = miniCartSimplifiedDataList[parentKey] as MiniCartItem2.MiniCartItemParentProduct
                            val products = HashMap(currentParentItem.products)
                            products[key] = item
                            val totalQuantity = currentParentItem.totalQuantity + item.quantity
                            miniCartSimplifiedDataList[parentKey] = MiniCartItem2.MiniCartItemParentProduct(
                                    cartDetail.product.parentId, totalQuantity, products
                            )
                        }
                    }
                }
            }
        }

        return miniCartSimplifiedDataList
    }

}
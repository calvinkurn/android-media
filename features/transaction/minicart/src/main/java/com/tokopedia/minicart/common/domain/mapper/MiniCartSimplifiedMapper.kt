package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.minicart.common.data.response.minicartlist.BeliButtonConfig
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import javax.inject.Inject
import kotlin.math.min

class MiniCartSimplifiedMapper @Inject constructor() {

    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): MiniCartSimplifiedData {
        return MiniCartSimplifiedData().apply {
            miniCartItems = mapMiniCartListData(miniCartData)
            isShowMiniCartWidget = miniCartItems.isNotEmpty()
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
        }
    }

    private fun mapMiniCartWidgetData(miniCartData: MiniCartData): MiniCartWidgetData {
        var totalQty = 0
        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                cartDetail.products.forEach { product ->
                    totalQty += product.productQuantity
                }
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

    private fun mapMiniCartListData(miniCartData: MiniCartData): Map<MiniCartItemKey, MiniCartItem> {
        val miniCartSimplifiedDataList = hashMapOf<MiniCartItemKey, MiniCartItem>()
        miniCartSimplifiedDataList.putAll(getAvailableData(miniCartData))
        getUnavailableData(miniCartData, miniCartSimplifiedDataList)
        return miniCartSimplifiedDataList
    }

    private fun getAvailableData(miniCartData: MiniCartData): Map<MiniCartItemKey, MiniCartItem> {
        val miniCartSimplifiedDataList = hashMapOf<MiniCartItemKey, MiniCartItem>()

        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach { cartDetail ->
                cartDetail.products.forEach { product ->
                    val item = MiniCartItem.MiniCartItemProduct().apply {
                        isError = false
                        cartId = product.cartId
                        productId = product.productId
                        productParentId = product.parentId
                        quantity = if (product.productSwitchInvenage == 0) {
                            product.productQuantity
                        } else {
                            min(product.productQuantity, product.productInvenageValue)
                        }
                        notes = product.productNotes
                        cartString = availableGroup.cartString
                        campaignId = product.campaignId
                        attribution = product.productTrackerData.attribution
                        productWeight = product.productWeight
                        productSlashPriceLabel = product.slashPriceLabel
                        warehouseId = product.warehouseId
                        shopId = availableGroup.shop.shopId
                        shopName = availableGroup.shop.shopName
                        shopType = availableGroup.shop.shopTypeInfo.titleFmt
                        categoryId = product.categoryId
                        freeShippingType =
                            if (availableGroup.shipmentInformation.freeShippingExtra.eligible) "bebas ongkir extra"
                            else if (availableGroup.shipmentInformation.freeShipping.eligible) "bebas ongkir"
                            else ""
                        category = product.category
                        productName = product.productName
                        productVariantName = product.variantDescriptionDetail.variantName.joinToString(", ")
                        productPrice = product.productPrice
                    }
                    val key = MiniCartItemKey(product.productId)
                    val bundleDetail = cartDetail.bundleDetail
                    if (bundleDetail.isBundlingItem()) {
                        val bundleKey = MiniCartItemKey(bundleDetail.bundleId, type = MiniCartItemType.BUNDLE)
                        if (!miniCartSimplifiedDataList.contains(bundleKey)) {
                            miniCartSimplifiedDataList[bundleKey] = MiniCartItem.MiniCartItemBundle(
                                    bundleId = bundleDetail.bundleId,
                                    bundleGroupId = bundleDetail.bundleGroupId,
                                    bundleTitle = bundleDetail.bundleName,
                                    bundlePrice = bundleDetail.bundlePrice,
                                    bundleSlashPriceLabel = bundleDetail.slashPriceLabel,
                                    bundleOriginalPrice = bundleDetail.bundleOriginalPrice,
                                    bundleQuantity = bundleDetail.bundleQty,
                                    bundleMultiplier = product.productQuantity / bundleDetail.bundleQty,
                                    bundleLabelQuantity = product.productQuantity / bundleDetail.bundleQty,
                                    products = hashMapOf(key to item)
                            )
                        } else {
                            val currentBundleItem = miniCartSimplifiedDataList[bundleKey] as MiniCartItem.MiniCartItemBundle
                            val products = HashMap(currentBundleItem.products)
                            products[key] = item
                            miniCartSimplifiedDataList[bundleKey] = currentBundleItem.copy(products = products)
                        }
                    } else {
                        miniCartSimplifiedDataList[key] = item
                    }
                    if (item.productParentId.isNotBlankOrZero()) {
                        val parentKey = MiniCartItemKey(product.parentId, type = MiniCartItemType.PARENT)
                        if (!miniCartSimplifiedDataList.contains(parentKey)) {
                            miniCartSimplifiedDataList[parentKey] = MiniCartItem.MiniCartItemParentProduct(
                                parentId = product.parentId,
                                totalQuantity = item.quantity,
                                products = hashMapOf(key to item)
                            )
                        } else {
                            val currentParentItem = miniCartSimplifiedDataList[parentKey] as MiniCartItem.MiniCartItemParentProduct
                            val products = HashMap(currentParentItem.products)
                            products[key] = item
                            val totalQuantity = currentParentItem.totalQuantity + item.quantity
                            miniCartSimplifiedDataList[parentKey] = MiniCartItem.MiniCartItemParentProduct(
                                product.parentId, totalQuantity, products
                            )
                        }
                    }
                }
            }
        }

        return miniCartSimplifiedDataList
    }

    private fun getUnavailableData(miniCartData: MiniCartData, miniCartSimplifiedDataList: MutableMap<MiniCartItemKey, MiniCartItem>): Map<MiniCartItemKey, MiniCartItem> {
        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    cartDetail.products.forEach { product ->
                        val item = MiniCartItem.MiniCartItemProduct().apply {
                            isError = true
                            cartId = product.cartId
                            productId = product.productId
                            productParentId = product.parentId
                            quantity = product.productQuantity
                            notes = product.productNotes
                            cartString = unavailableGroup.cartString
                        }
                        val key = MiniCartItemKey(product.productId)
                        val bundleDetail = cartDetail.bundleDetail
                        if (bundleDetail.isBundlingItem()) {
                            val bundleKey = MiniCartItemKey(bundleDetail.bundleId, type = MiniCartItemType.BUNDLE)
                            if (!miniCartSimplifiedDataList.contains(bundleKey)) {
                                miniCartSimplifiedDataList[bundleKey] = MiniCartItem.MiniCartItemBundle(
                                        isError = true,
                                        bundleId = bundleDetail.bundleId,
                                        bundleGroupId = bundleDetail.bundleGroupId,
                                        bundleTitle = bundleDetail.bundleName,
                                        bundlePrice = bundleDetail.bundlePrice,
                                        bundleSlashPriceLabel = bundleDetail.slashPriceLabel,
                                        bundleOriginalPrice = bundleDetail.bundleOriginalPrice,
                                        bundleQuantity = bundleDetail.bundleQty,
                                        bundleMultiplier = product.productQuantity / bundleDetail.bundleQty,
                                        bundleLabelQuantity = product.productQuantity / bundleDetail.bundleQty,
                                        products = hashMapOf(key to item)
                                )
                            } else {
                                val currentBundleItem = miniCartSimplifiedDataList[bundleKey] as MiniCartItem.MiniCartItemBundle
                                val products = HashMap(currentBundleItem.products)
                                products[key] = item
                                miniCartSimplifiedDataList[bundleKey] = currentBundleItem.copy(products = products)
                            }
                        } else if (!miniCartSimplifiedDataList.contains(key)) {
                            miniCartSimplifiedDataList[key] = item
                        }
                        if (item.productParentId.isNotBlankOrZero()) {
                            val parentKey = MiniCartItemKey(product.parentId, type = MiniCartItemType.PARENT)
                            if (!miniCartSimplifiedDataList.contains(parentKey)) {
                                miniCartSimplifiedDataList[parentKey] = MiniCartItem.MiniCartItemParentProduct(
                                    parentId = product.parentId,
                                    totalQuantity = item.quantity,
                                    products = hashMapOf(key to item)
                                )
                            } else {
                                val currentParentItem = miniCartSimplifiedDataList[parentKey] as MiniCartItem.MiniCartItemParentProduct
                                val products = HashMap(currentParentItem.products)
                                products[key] = item
                                val totalQuantity = currentParentItem.totalQuantity + item.quantity
                                miniCartSimplifiedDataList[parentKey] = MiniCartItem.MiniCartItemParentProduct(
                                    product.parentId, totalQuantity, products
                                )
                            }
                        }
                    }
                }
            }
        }

        return miniCartSimplifiedDataList
    }

}
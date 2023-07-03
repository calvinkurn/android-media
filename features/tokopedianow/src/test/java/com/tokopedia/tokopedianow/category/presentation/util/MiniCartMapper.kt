package com.tokopedia.tokopedianow.category.presentation.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.data.response.minicartlist.BeliButtonConfig
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.data.ShoppingSummaryBottomSheetData
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel
import kotlin.math.min

object MiniCartMapper {
    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): MiniCartSimplifiedData {
        return MiniCartSimplifiedData().apply {
            miniCartItems = mapMiniCartListData(miniCartData)
            isShowMiniCartWidget = miniCartItems.isNotEmpty()
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
            shoppingSummaryBottomSheetData = mapShoppingSummaryData(miniCartData)
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
            headlineWording = miniCartData.data.bottomBar.text
            totalProductPriceWording = miniCartData.data.bottomBar.totalPriceFmt
            isShopActive = miniCartData.data.bottomBar.isShopActive
        }
    }

    private fun mapMiniCartListData(miniCartData: MiniCartData): Map<MiniCartItemKey, MiniCartItem> {
        val miniCartSimplifiedDataList = hashMapOf<MiniCartItemKey, MiniCartItem>()
        miniCartSimplifiedDataList.putAll(getAvailableData(miniCartData))
        miniCartSimplifiedDataList.putAll(getUnavailableData(miniCartData, miniCartSimplifiedDataList))
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
                    }
                    val key = MiniCartItemKey(product.productId)
                    val bundleDetail = cartDetail.bundleDetail
                    if (bundleDetail.isBundlingItem()) {
                        val bundleKey = MiniCartItemKey(bundleDetail.bundleGroupId, type = MiniCartItemType.BUNDLE)
                        if (!miniCartSimplifiedDataList.contains(bundleKey)) {
                            miniCartSimplifiedDataList[bundleKey] = MiniCartItem.MiniCartItemBundleGroup(
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
                            val currentBundleItem = miniCartSimplifiedDataList[bundleKey] as MiniCartItem.MiniCartItemBundleGroup
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
                                product.parentId,
                                totalQuantity,
                                products
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
                            val bundleKey = MiniCartItemKey(bundleDetail.bundleGroupId, type = MiniCartItemType.BUNDLE)
                            if (!miniCartSimplifiedDataList.contains(bundleKey)) {
                                miniCartSimplifiedDataList[bundleKey] = MiniCartItem.MiniCartItemBundleGroup(
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
                                val currentBundleItem = miniCartSimplifiedDataList[bundleKey] as MiniCartItem.MiniCartItemBundleGroup
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
                                    product.parentId,
                                    totalQuantity,
                                    products
                                )
                            }
                        }
                    }
                }
            }
        }

        return miniCartSimplifiedDataList
    }

    private fun mapShoppingSummaryData(miniCartData: MiniCartData): ShoppingSummaryBottomSheetData {
        val shoppingSummaryItems = mutableListOf<Visitable<*>>()
        miniCartData.data.simplifiedShoppingSummary.sections.forEachIndexed { idx, section ->
            if (section.title.isNotBlank() && section.details.isNotEmpty()) {
                shoppingSummaryItems.add(ShoppingSummaryHeaderUiModel(section.iconUrl, section.title, section.description))
                section.details.forEach { sectionDetailItem ->
                    shoppingSummaryItems.add(ShoppingSummaryProductUiModel(sectionDetailItem.name, sectionDetailItem.value))
                }
                shoppingSummaryItems.add(ShoppingSummarySeparatorUiModel())
            } else if (section.title.isBlank() && idx == miniCartData.data.simplifiedShoppingSummary.sections.lastIndex) {
                section.details.forEach { sectionDetailItem ->
                    shoppingSummaryItems.add(ShoppingSummaryTotalTransactionUiModel(sectionDetailItem.name, sectionDetailItem.value))
                }
            }
        }
        return ShoppingSummaryBottomSheetData(
            title = miniCartData.data.simplifiedShoppingSummary.text,
            items = shoppingSummaryItems
        )
    }

    private fun String.isNotBlankOrZero(): Boolean {
        return this.isNotBlank() && this.toLongOrZero() != 0L
    }
}

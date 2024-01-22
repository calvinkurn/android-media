package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.cartcommon.data.response.bmgm.BmGmData
import com.tokopedia.cartcommon.data.response.bmgm.BmGmProduct
import com.tokopedia.cartcommon.data.response.bmgm.BmGmProductBenefit
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.common.data.response.minicartlist.BeliButtonConfig
import com.tokopedia.minicart.common.data.response.minicartlist.CartDetail
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.minicart.common.data.response.minicartlist.ShoppingSummary
import com.tokopedia.minicart.common.data.response.minicartlist.WholesalePrice
import com.tokopedia.minicart.common.domain.data.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.data.ProductBenefitUiModel
import com.tokopedia.minicart.common.domain.data.ProductUiModel
import com.tokopedia.minicart.common.domain.data.ShoppingSummaryBottomSheetData
import com.tokopedia.minicart.common.domain.data.TierUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import kotlin.math.min

object MiniCartSimplifiedMapper {

    fun mapMiniCartSimplifiedData(miniCartData: MiniCartData): MiniCartSimplifiedData {
        return MiniCartSimplifiedData().apply {
            miniCartItems = mapMiniCartListData(miniCartData)
            isShowMiniCartWidget = miniCartItems.isNotEmpty()
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
            shoppingSummaryBottomSheetData = mapShoppingSummaryData(miniCartData)
            bmgmData = mapBmgm(miniCartData)
        }
    }

    fun getMiniCartDetail(data: MiniCartData): CartDetail {
        data.data.availableSection.availableGroup.forEach { group ->
            val detail = group.cartDetails.firstOrNull { detail ->
                val cartDetailInfo = detail.cartDetailInfo
                cartDetailInfo.cartDetailType.equals(CART_DETAIL_TYPE, true) &&
                        cartDetailInfo.bmgmData.offerId != Long.ZERO
            }
            if (detail != null) {
                return detail
            }
        }
        return CartDetail()
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
            totalProductOriginalPrice = miniCartData.data.shoppingSummary.totalOriginalValue
            totalProductError = miniCartData.data.totalProductError
            containsOnlyUnavailableItems =
                miniCartData.data.availableSection.availableGroup.isEmpty() && miniCartData.data.unavailableSection.isNotEmpty()
            unavailableItemsCount = miniCartData.data.totalProductError
            isOCCFlow =
                miniCartData.data.beliButtonConfig.buttonType == BeliButtonConfig.BUTTON_TYPE_OCC
            buttonBuyWording = miniCartData.data.beliButtonConfig.buttonWording
            headlineWording = miniCartData.data.bottomBar.text
            totalProductPriceWording = miniCartData.data.bottomBar.totalPriceFmt
            isShopActive = miniCartData.data.bottomBar.isShopActive
        }
    }

    private fun mapMiniCartListData(miniCartData: MiniCartData): Map<MiniCartItemKey, MiniCartItem> {
        val miniCartSimplifiedDataList = hashMapOf<MiniCartItemKey, MiniCartItem>()
        miniCartSimplifiedDataList.putAll(getAvailableData(miniCartData))
        miniCartSimplifiedDataList.putAll(
            getUnavailableData(
                miniCartData,
                miniCartSimplifiedDataList
            )
        )
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
                        freeShippingType = product.freeShippingGeneral.boName
                        category = product.category
                        productName = product.productName
                        productVariantName =
                            product.variantDescriptionDetail.variantName.joinToString(", ")
                        productPrice = product.productPrice
                    }
                    val key = MiniCartItemKey(product.productId)
                    val bundleDetail = cartDetail.bundleDetail
                    if (bundleDetail.isBundlingItem()) {
                        val bundleKey = MiniCartItemKey(
                            bundleDetail.bundleGroupId,
                            type = MiniCartItemType.BUNDLE
                        )
                        if (!miniCartSimplifiedDataList.contains(bundleKey)) {
                            miniCartSimplifiedDataList[bundleKey] =
                                MiniCartItem.MiniCartItemBundleGroup(
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
                            val currentBundleItem =
                                miniCartSimplifiedDataList[bundleKey] as MiniCartItem.MiniCartItemBundleGroup
                            val products = HashMap(currentBundleItem.products)
                            products[key] = item
                            miniCartSimplifiedDataList[bundleKey] =
                                currentBundleItem.copy(products = products)
                        }
                    } else {
                        miniCartSimplifiedDataList[key] = item
                    }
                    if (item.productParentId.isNotBlankOrZero()) {
                        val parentKey =
                            MiniCartItemKey(product.parentId, type = MiniCartItemType.PARENT)
                        if (!miniCartSimplifiedDataList.contains(parentKey)) {
                            miniCartSimplifiedDataList[parentKey] =
                                MiniCartItem.MiniCartItemParentProduct(
                                    parentId = product.parentId,
                                    totalQuantity = item.quantity,
                                    products = hashMapOf(key to item)
                                )
                        } else {
                            val currentParentItem =
                                miniCartSimplifiedDataList[parentKey] as MiniCartItem.MiniCartItemParentProduct
                            val products = HashMap(currentParentItem.products)
                            products[key] = item
                            val totalQuantity = currentParentItem.totalQuantity + item.quantity
                            miniCartSimplifiedDataList[parentKey] =
                                MiniCartItem.MiniCartItemParentProduct(
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

    private fun getUnavailableData(
        miniCartData: MiniCartData,
        miniCartSimplifiedDataList: MutableMap<MiniCartItemKey, MiniCartItem>
    ): Map<MiniCartItemKey, MiniCartItem> {
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
                            val bundleKey = MiniCartItemKey(
                                bundleDetail.bundleGroupId,
                                type = MiniCartItemType.BUNDLE
                            )
                            if (!miniCartSimplifiedDataList.contains(bundleKey)) {
                                miniCartSimplifiedDataList[bundleKey] =
                                    MiniCartItem.MiniCartItemBundleGroup(
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
                                val currentBundleItem =
                                    miniCartSimplifiedDataList[bundleKey] as MiniCartItem.MiniCartItemBundleGroup
                                val products = HashMap(currentBundleItem.products)
                                products[key] = item
                                miniCartSimplifiedDataList[bundleKey] =
                                    currentBundleItem.copy(products = products)
                            }
                        } else if (!miniCartSimplifiedDataList.contains(key)) {
                            miniCartSimplifiedDataList[key] = item
                        }
                        if (item.productParentId.isNotBlankOrZero()) {
                            val parentKey =
                                MiniCartItemKey(product.parentId, type = MiniCartItemType.PARENT)
                            if (!miniCartSimplifiedDataList.contains(parentKey)) {
                                miniCartSimplifiedDataList[parentKey] =
                                    MiniCartItem.MiniCartItemParentProduct(
                                        parentId = product.parentId,
                                        totalQuantity = item.quantity,
                                        products = hashMapOf(key to item)
                                    )
                            } else {
                                val currentParentItem =
                                    miniCartSimplifiedDataList[parentKey] as MiniCartItem.MiniCartItemParentProduct
                                val products = HashMap(currentParentItem.products)
                                products[key] = item
                                val totalQuantity = currentParentItem.totalQuantity + item.quantity
                                miniCartSimplifiedDataList[parentKey] =
                                    MiniCartItem.MiniCartItemParentProduct(
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
                shoppingSummaryItems.add(
                    ShoppingSummaryHeaderUiModel(
                        section.iconUrl,
                        section.title,
                        section.description
                    )
                )
                section.details.forEach { sectionDetailItem ->
                    shoppingSummaryItems.add(
                        ShoppingSummaryProductUiModel(
                            sectionDetailItem.name,
                            sectionDetailItem.value
                        )
                    )
                }
                shoppingSummaryItems.add(ShoppingSummarySeparatorUiModel())
            } else if (section.title.isBlank() && idx == miniCartData.data.simplifiedShoppingSummary.sections.lastIndex) {
                section.details.forEach { sectionDetailItem ->
                    shoppingSummaryItems.add(
                        ShoppingSummaryTotalTransactionUiModel(
                            sectionDetailItem.name,
                            sectionDetailItem.value
                        )
                    )
                }
            }
        }
        return ShoppingSummaryBottomSheetData(
            title = miniCartData.data.simplifiedShoppingSummary.text,
            items = shoppingSummaryItems
        )
    }

    private fun mapBmgm(data: MiniCartData): BmgmMiniCartDataUiModel {
        val shoppingSummary = data.data.shoppingSummary
        val detail = getMiniCartDetail(data)
        val productListMap = detail.products.associateBy { it.productId }
        return getMiniCartData(
            detail.cartDetailInfo.bmgmData,
            shoppingSummary,
            productListMap
        )
    }

    private fun getMiniCartData(
        bmgmData: BmGmData,
        shoppingSummary: ShoppingSummary,
        productListMap: Map<String, Product>
    ): BmgmMiniCartDataUiModel {
        val hasReachMaxDiscount = bmgmData.offerStatus == OFFER_STATUS_HAS_REACH_MAX_DISC
        return BmgmMiniCartDataUiModel(
            offerId = bmgmData.offerId,
            offerTypeId = bmgmData.offerTypeId,
            offerMessage = bmgmData.offerMessage,
            hasReachMaxDiscount = hasReachMaxDiscount,
            priceBeforeBenefit = shoppingSummary.totalOriginalValue,
            finalPrice = shoppingSummary.totalValue,
            tiersApplied = bmgmData.tierProductList.map { tier ->
                TierUiModel(
                    tierId = tier.tierId,
                    tierMessage = tier.tierMessage,
                    tierDiscountStr = tier.tierDiscountText,
                    priceBeforeBenefit = tier.priceBeforeBenefit,
                    priceAfterBenefit = tier.priceAfterBenefit,
                    products = getProductList(tier.listProduct, productListMap),
                    benefitWording = tier.benefitWording,
                    actionWording = tier.actionWording,
                    benefitQuantity = tier.benefitQuantity,
                    benefitProducts = getBenefitProductList(tier.productsBenefit)
                )
            }
        )
    }

    private fun getProductList(
        tierProductList: List<BmGmProduct>,
        productListMap: Map<String, Product>
    ): List<ProductUiModel> {
        val products = mutableListOf<ProductUiModel>()
        tierProductList.forEach { tierProduct ->
            productListMap[tierProduct.productId]?.let { p ->
                val product = ProductUiModel(
                    productId = p.productId,
                    warehouseId = tierProduct.warehouseId.toString(),
                    productName = p.productName,
                    productImage = p.productImage.imageSrc100Square,
                    cartId = p.cartId,
                    finalPrice = getFinalPrice(
                        p.productQuantity,
                        p.wholesalePrice,
                        tierProduct.priceBeforeBenefit
                    ),
                    quantity = tierProduct.quantity
                )
                repeat(tierProduct.quantity) {
                    products.add(product)
                }
            }
        }
        return products
    }

    private fun getBenefitProductList(
        tierProductBenefitList: List<BmGmProductBenefit>
    ): List<ProductBenefitUiModel> {
        return tierProductBenefitList.map {
            ProductBenefitUiModel(
                productId = it.productId,
                quantity = it.quantity,
                productName = it.productName,
                productImage = it.productImage,
                originalPrice = it.originalPrice,
                finalPrice = it.finalPrice
            )
        }
    }

    private fun getFinalPrice(
        productQuantity: Int,
        wholesalePrice: List<WholesalePrice>,
        priceBeforeBenefit: Double
    ): Double {
        wholesalePrice.forEach {
            if (productQuantity >= it.qtyMin && productQuantity <= it.qtyMax) {
                return it.prdPrc
            }
        }
        return priceBeforeBenefit
    }

    private const val CART_DETAIL_TYPE = "bmgm"
    private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2
}

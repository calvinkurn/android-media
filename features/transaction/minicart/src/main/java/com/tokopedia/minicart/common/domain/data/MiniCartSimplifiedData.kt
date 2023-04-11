package com.tokopedia.minicart.common.domain.data

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class MiniCartSimplifiedData(
    var miniCartWidgetData: MiniCartWidgetData = MiniCartWidgetData(),
    var miniCartItems: Map<MiniCartItemKey, MiniCartItem> = emptyMap(),
    var isShowMiniCartWidget: Boolean = false,
    var shoppingSummaryBottomSheetData: ShoppingSummaryBottomSheetData = ShoppingSummaryBottomSheetData()
)

fun Map<MiniCartItemKey, MiniCartItem>.getMiniCartItemProduct(productId: String): MiniCartItem.MiniCartItemProduct? {
    return get(MiniCartItemKey(productId)) as? MiniCartItem.MiniCartItemProduct
}

fun Map<MiniCartItemKey, MiniCartItem>.getMiniCartItemBundleGroup(bundleGroupId: String): MiniCartItem.MiniCartItemBundleGroup? {
    return get(MiniCartItemKey(bundleGroupId, MiniCartItemType.BUNDLE)) as? MiniCartItem.MiniCartItemBundleGroup
}

fun Map<MiniCartItemKey, MiniCartItem>.getMiniCartItemParentProduct(parentId: String): MiniCartItem.MiniCartItemParentProduct? {
    return get(MiniCartItemKey(parentId, MiniCartItemType.PARENT)) as? MiniCartItem.MiniCartItemParentProduct
}

fun Map<MiniCartItemKey, MiniCartItem>.mapProductsWithProductId(): Map<String, MiniCartItem.MiniCartItemProduct> {
    val map: HashMap<String, MiniCartItem.MiniCartItemProduct> = HashMap()
    values.forEach {
        if (it is MiniCartItem.MiniCartItemProduct) {
            map[it.productId] = it
        }
    }
    return map
}

data class MiniCartWidgetData(
    var totalProductCount: Int = 0,
    var totalProductPrice: Double = 0.0,
    var totalProductError: Int = 0,
    var containsOnlyUnavailableItems: Boolean = false,
    var unavailableItemsCount: Int = 0,
    var isOCCFlow: Boolean = false,
    var buttonBuyWording: String = "",
    var headlineWording: String = "",
    var totalProductPriceWording: String = "",
    var isShopActive: Boolean = false
)

data class MiniCartItemKey(
    var id: String,
    var type: MiniCartItemType = MiniCartItemType.PRODUCT
)

sealed class MiniCartItemType {
    object PRODUCT : MiniCartItemType()
    object BUNDLE : MiniCartItemType()
    object PARENT : MiniCartItemType()
}

sealed class MiniCartItem {
    data class MiniCartItemBundleGroup(
        var isError: Boolean = false,
        var bundleId: String = "",
        var bundleGroupId: String = "",
        var bundleTitle: String = "",
        var bundlePrice: Double = 0.0,
        var bundleSlashPriceLabel: String = "",
        var bundleOriginalPrice: Double = 0.0,
        var bundleQuantity: Int = 0,
        var bundleLabelQuantity: Int = 0,
        var bundleMultiplier: Int = 0,
        var products: Map<MiniCartItemKey, MiniCartItemProduct> = emptyMap()
    ) : MiniCartItem()

    data class MiniCartItemProduct(
        var isError: Boolean = false,
        var cartId: String = "",
        var productId: String = "",
        var productParentId: String = "",
        var quantity: Int = 0,
        var notes: String = "",
        var cartString: String = "",

        // Fields below are for analytics & atc occ purpose only
        internal var campaignId: String = "",
        internal var attribution: String = "",
        internal var productWeight: Int = 0,
        internal var productSlashPriceLabel: String = "",
        internal var warehouseId: String = "",
        internal var shopId: String = "",
        internal var shopName: String = "",
        internal var shopType: String = "",
        internal var categoryId: String = "",
        internal var freeShippingType: String = "",
        internal var category: String = "",
        internal var productName: String = "",
        internal var productVariantName: String = "",
        internal var productPrice: Double = 0.0
    ) : MiniCartItem()

    data class MiniCartItemParentProduct(
        var parentId: String = "",
        var totalQuantity: Int = 0,
        var products: Map<MiniCartItemKey, MiniCartItemProduct> = emptyMap()
    ) : MiniCartItem()
}

data class ShoppingSummaryBottomSheetData(
    var title: String = "",
    var items: List<Visitable<*>> = emptyList()
)

package com.tokopedia.minicart.common.domain.data

data class MiniCartSimplifiedData(
        var miniCartWidgetData: MiniCartWidgetData = MiniCartWidgetData(),
        var miniCartItems: List<MiniCartItem> = emptyList(),
        var isShowMiniCartWidget: Boolean = false
) {

    fun getMiniCartItemByProductId(productId: String): MiniCartItem? {
        loop@ for (miniCartItem in miniCartItems) {
            if (miniCartItem.productId == productId) {
                return miniCartItem
            }
        }

        return null
    }

}

data class MiniCartSimplifiedData2(
        var miniCartWidgetData: MiniCartWidgetData = MiniCartWidgetData(),
        var miniCartItems: Map<MiniCartItemKey, MiniCartItem2> = emptyMap(),
        var isShowMiniCartWidget: Boolean = false
) {

    fun getMiniCartItemProductByProductId(productId: String): MiniCartItem2.MiniCartItemProduct? {
        return miniCartItems[MiniCartItemKey(productId)] as? MiniCartItem2.MiniCartItemProduct
    }

    fun getMiniCartItemBundleByProductId(bundleId: String): MiniCartItem2.MiniCartItemProduct? {
        return miniCartItems[MiniCartItemKey(bundleId, MiniCartItemType.BUNDLE)] as? MiniCartItem2.MiniCartItemProduct
    }

    fun getMiniCartItemParentProductByProductId(bundleId: String): MiniCartItem2.MiniCartItemParentProduct? {
        return miniCartItems[MiniCartItemKey(bundleId, MiniCartItemType.PARENT)] as? MiniCartItem2.MiniCartItemParentProduct
    }
}

data class MiniCartWidgetData(
        var totalProductCount: Int = 0,
        var totalProductPrice: Long = 0,
        var totalProductError: Int = 0,
        var containsOnlyUnavailableItems: Boolean = false,
        var unavailableItemsCount: Int = 0,
        var isOCCFlow: Boolean = false,
        var buttonBuyWording: String = ""
)

data class MiniCartItem(
        var isError: Boolean = false,
        var cartId: String = "",
        var productId: String = "",
        var productParentId: String = "",
        var quantity: Int = 0,
        var notes: String = "",

        // Fields below are for analytics & atc occ purpose only
        var campaignId: String = "",
        var attribution: String = "",
        var productWeight: Int = 0,
        var productSlashPriceLabel: String = "",
        var warehouseId: String = "",
        var shopId: String = "",
        var shopName: String = "",
        var shopType: String = "",
        var categoryId: String = "",
        var freeShippingType: String = "",
        var category: String = "",
        var productName: String = "",
        var productVariantName: String = "",
        var productPrice: Long = 0L
)

data class MiniCartItemKey(
        var id: String,
        var type: MiniCartItemType = MiniCartItemType.PRODUCT,
)

sealed class MiniCartItemType {
    object PRODUCT: MiniCartItemType()
    object BUNDLE: MiniCartItemType()
    object PARENT: MiniCartItemType()
}

sealed class MiniCartItem2 {
    data class MiniCartItemBundle(
            var isError: Boolean = false,
            var bundleId: String = "",
            var bundleGroupId: String = "",
            var bundleTitle: String = "",
            var bundlePrice: Long = 0L,
            var bundleSlashPriceLabel: String = "",
            var bundleOriginalPrice: Long = 0L,
            var bundleQuantity: Int = 0,
            var editBundleApplink: String = "",
            var bundleIconUrl: String = "",
            var bundleLabelQuantity: Int = 0,
            var products: Map<MiniCartItemKey, MiniCartItemProduct> = emptyMap()
    ): MiniCartItem2()

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
            internal var productPrice: Long = 0L
    ): MiniCartItem2()

    data class MiniCartItemParentProduct(
            var parentId: String = "",
            var totalQuantity: Int = 0,
            var products: Map<MiniCartItemKey, MiniCartItemProduct> = emptyMap(),
    ): MiniCartItem2()
}
package com.tokopedia.minicart.common.domain.data

data class MiniCartSimplifiedData(
        var miniCartWidgetData: MiniCartWidgetData = MiniCartWidgetData(),
        var miniCartItems: List<MiniCartItem> = emptyList(),
        var isShowMiniCartWidget: Boolean = false
)

data class MiniCartWidgetData(
        var totalProductCount: Int = 0,
        var totalProductPrice: Long = 0
)

data class MiniCartItem(
        var isError: Boolean = false,
        var cartId: String = "",
        var productId: String = "",
        var productParentId: String = "",
        var quantity: Int = 0,
        var notes: String = "",

        // Fields below are for analytics purpose only
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
        var productPrice: Long = 0L,
        var productQty: Int = 0
)
package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

data class OrderProduct(
        var parentId: Int = 0,
        var productName: String = "",
        var productPrice: Int = 0,
        var productImageUrl: String = "",
        var productChildrenList: ArrayList<OrderProductChild> = arrayListOf(),
        var selectedVariantOptionsIdMap: LinkedHashMap<Int, Int> = LinkedHashMap(),
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var originalPrice: Int = productPrice,
        var discountedPercentage: Float = 0f,
        var isFreeOngkir: Boolean = false,
        var freeOngkirImg: String = "",
        var quantity: QuantityUiModel? = null,
        var typeVariantList: ArrayList<VariantUiModel>? = null)

data class OrderProductChild(
        var productId: Int,
        var productName: String,
        var productPrice: Int,
        var productImageUrl: String,
        var isAvailable: Boolean,
        var isSelected: Boolean,
        var stockWording: String,
        var stock: Int,
        var minOrder: Int,
        var maxOrder: Int,
        var optionsId: ArrayList<Int>
)

data class QuantityUiModel(
        var stockWording: String,
        var maxOrderQuantity: Int,
        var minOrderQuantity: Int,
        var orderQuantity: Int,
        var errorFieldBetween: String,
        var errorFieldMaxChar: String,
        var errorFieldRequired: String,
        var errorProductAvailableStock: String,
        var errorProductAvailableStockDetail: String,
        var errorProductMaxQuantity: String,
        var errorProductMinQuantity: String,
        var isStateError: Boolean,
        var stockFromWarehouse: Int,
        var stockWordingFromWarehouse: String
)
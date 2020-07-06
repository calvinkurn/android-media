package com.tokopedia.oneclickcheckout.order.view.model

data class OrderProduct(
        var parentId: Int = 0,
        var productId: Int = 0,
        var productName: String = "",
        var productPrice: Long = 0,
        var wholesalePrice: List<WholesalePrice> = arrayListOf(),
        var productImageUrl: String = "",
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var originalPrice: String = "",
        var discountedPercentage: Float = 0f,
        var isFreeOngkir: Boolean = false,
        var freeOngkirImg: String = "",
        var weight: Int = 0,
        var quantity: QuantityUiModel = QuantityUiModel(),
        var notes: String = "",
        var cashback: String = "",
        var warehouseId: Int = 0,
        var isPreorder: Int = 0,
        var categoryId: Int = 0,
        var category: String = "",
        var productFinsurance: Int = 0,
        var isSlashPrice: Boolean = false,
        var productTrackerData: ProductTrackerData = ProductTrackerData(),
        var productInvenageTotal: ProductInvenageTotal = ProductInvenageTotal()
)

data class WholesalePrice(
        val qtyMinFmt: String = "",
        val qtyMaxFmt: String = "",
        val prdPrcFmt: String = "",
        val qtyMin: Int = 0,
        val qtyMax: Int = 0,
        val prdPrc: Int = 0
)

data class ProductTrackerData(
        var attribution: String = "",
        var trackerListName: String = ""
)

data class QuantityUiModel(
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var orderQuantity: Int = 0,
        var errorProductAvailableStock: String = "",
        var errorProductMaxQuantity: String = "",
        var errorProductMinQuantity: String = "",
        var isStateError: Boolean = false,
        var maxOrderStock: Int = 0
) {
    companion object {
        const val VALUE_REPLACE_STRING = "{{value}}"
    }
}

data class ProductInvenageTotal(
        val isCountedByProduct: Boolean = false,
        val isCountedByUser: Boolean = false,
        val byProduct: ByProduct = ByProduct(),
        val byProductText: ByProductText = ByProductText(),
        val byUser: ByUser = ByUser(),
        val byUserText: ByUserText = ByUserText()
)

data class ByProduct(
        val inCart: Int = 0,
        val lastStockLessThan: Int = 0
)

data class ByProductText(
        val inCart: String = "",
        val lastStockLessThan: String = "",
        val complete: String = ""
)

data class ByUser(
        val inCart: Int = 0,
        val lastStockLessThan: Int = 0
)

data class ByUserText(
        val inCart: String = "",
        val lastStockLessThan: String = "",
        val complete: String = ""
)
package com.tokopedia.common_tradein.model

class TradeInPDPData(
    var shopID: String? = null,
    var shopName: String? = null,
    var shopBadge: String? = null,
    var shopLocation: String? = null,
    var productId: String = "0",
    var productPrice: Double = 0.0,
    var productName: String? = null,
    var productImage: String? = null,
    var minOrder: Int = 0,
    val selectedWarehouseId: Int = 0,
    val trackerAttributionPdp: String? = null,
    val trackerListNamePdp: String? = null,
    val shippingMinimumPrice: Double = 0.0,
    val getProductName: String? = null,
    val categoryName: String? = null
) {
}
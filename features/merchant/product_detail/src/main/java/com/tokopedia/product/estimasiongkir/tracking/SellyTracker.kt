package com.tokopedia.product.estimasiongkir.tracking

class SellyTracker {
    data class ImpressionComponent(
        val buyerDistrictId: String,
        val sellerDistrictId: String,
        val prices: List<Pair<String, String>>,
        val layoutId: String,
        val beratSatuan: String,
        val productId: String,
        val shopId: String,
        val userId: String
    )
}

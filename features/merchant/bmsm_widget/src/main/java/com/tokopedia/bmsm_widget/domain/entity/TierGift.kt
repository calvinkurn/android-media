package com.tokopedia.bmsm_widget.domain.entity


data class TierGift(
    val tierId: Long,
    val tierName: String,
    val tierMessage: String,
    val maxBenefitQty: Int,
    val isEligible: Boolean,
    val products: List<GiftProduct>
) {
    data class GiftProduct(
        val productId: Long,
        val warehouseId: Long,
        val quantity: Int,
        val stock: Long,
        val productName: String,
        val productImageUrl: String,
        val originalPrice: Long,
        val finalPrice: Long,
        val isOutOfStock: Boolean
    )
}



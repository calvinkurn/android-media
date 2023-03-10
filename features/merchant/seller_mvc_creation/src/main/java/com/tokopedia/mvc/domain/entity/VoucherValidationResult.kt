package com.tokopedia.mvc.domain.entity

data class VoucherValidationResult(
    val totalAvailableQuota: Int,
    val validationDate: List<ValidationDate>,
    val validationError: ValidationError,
    val validationProduct: List<ValidationProduct>
) {
    data class ValidationDate(
        val dateEnd: String,
        val dateStart: String,
        val hourEnd: String,
        val hourStart: String,
        val totalLiveTime: String,
        val available: Boolean,
        val notAvailableReason: String,
        val type: Int
    )

    data class ValidationError(
        val benefitIdr: String,
        val benefitMax: String,
        val benefitPercent: String,
        val benefitType: String,
        val code: String,
        val couponName: String,
        val couponType: String,
        val dateEnd: String,
        val dateStart: String,
        val hourEnd: String,
        val hourStart: String,
        val image: String,
        val imageSquare: String,
        val isPublic: String,
        val minPurchase: String,
        val minPurchaseType: String,
        val minimumTierLevel: String,
        val quota: String
    )

    data class ValidationProduct(
        val isEligible: Boolean,
        val isVariant: Boolean,
        val parentProductId: Long,
        val reason: String,
        val variant: List<ProductVariant>
    ) {
        data class ProductVariant(
            val productId: Long,
            val productName: String,
            val price: Long,
            val stock: Int,
            val isEligible: Boolean,
            val reason: String
        )
    }
}

package com.tokopedia.shop.flashsale.domain.entity

data class ProductSubmissionResult(
    val isSuccess: Boolean,
    val errorMessage: String,
    val failedProducts: List<FailedProduct>
) {
    data class FailedProduct(val productId: String, val message: String)
}
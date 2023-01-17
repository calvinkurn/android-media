package com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel

data class ValidationResult(
    val isPriceError: Boolean = false,
    val isPricePercentError: Boolean = false,
    val isStockError: Boolean = false,
    val priceMessage: String,
    val pricePercentMessage: String
) {
    fun isAllFieldValid() = !isPriceError && !isPricePercentError && !isStockError
}

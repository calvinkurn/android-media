package com.tokopedia.oldminicart.common.promo.domain.data

data class ValidateUseMvcData(
        val status: String = "",
        val success: Boolean = false,
        val errorMessage: List<String> = emptyList(),
        val currentPurchase: Long = 0,
        val minimumPurchase: Long = 0,
        val progressPercentage: Int = 0,
        val message: String = "",
) {
    val isError
        get() = !status.equals("OK", ignoreCase = true)
}

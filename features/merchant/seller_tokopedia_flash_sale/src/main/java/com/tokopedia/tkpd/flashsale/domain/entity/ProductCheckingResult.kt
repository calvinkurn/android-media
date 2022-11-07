package com.tokopedia.tkpd.flashsale.domain.entity

data class ProductCheckingResult(
    val productId: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val isMultiloc: Boolean = false,
    val soldCount: Int? = null,
    val checkingDetailResult: CheckingDetailResult = CheckingDetailResult(),
    val locationCheckingResult: List<LocationCheckingResult> = emptyList()
) {
    data class CheckingDetailResult (
        val discountedPrice: Long = 0,
        val originalPrice: Long = 0,
        val discountPercent: Int = 0,
        val stock: Long = 0,
        val statusId: Long = 0,
        val statusText: String = "",
        val statusLabelType: Int = 0,
        val isSubsidy: Boolean = false,
        val subsidyAmount: Long = 0,
        val rejectionReason: String = ""
    )

    data class LocationCheckingResult (
        val warehouseId: String = "",
        val cityName: String = "",
        val soldCount: Int? = null,
        val checkingDetailResult: CheckingDetailResult = CheckingDetailResult()
    )
}

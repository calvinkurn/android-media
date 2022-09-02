package com.tokopedia.shop.flashsale.domain.entity.enums

data class ProductInputValidationResult(
    val errorList: List<ManageProductErrorType> = emptyList(),
    val maxPrice: Int = 0,
    val minPrice: Int = 0,
    val maxStock: Long = 0,
    val minStock: Int = 0,
    val maxOrder: Long = 0,
    val minOrder: Long = 0,
    val maxPricePercent: Long = 0,
    val minPricePercent: Long = 0
)

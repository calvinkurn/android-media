package com.tokopedia.tokopedianow.category.presentation.model

data class CategoryAtcTrackerModel(
    val categoryIdL1: String = "",
    val index: Int = 0,
    val productId: String = "",
    val warehouseId: String = "",
    val isOos: Boolean = false,
    val name: String = "",
    val price: Int = 0,
    val headerName: String = "",
    val quantity: Int = 0,
    val layoutType: String = "",
    val data: Any? = null
)

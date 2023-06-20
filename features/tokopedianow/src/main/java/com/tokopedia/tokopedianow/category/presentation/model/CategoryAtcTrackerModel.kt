package com.tokopedia.tokopedianow.category.presentation.model

data class CategoryAtcTrackerModel(
    val categoryIdL1: String,
    val index: Int,
    val productId: String,
    val warehouseId: String,
    val isOos: Boolean,
    val name: String,
    val price: Int,
    val headerName: String,
    val quantity: Int
)

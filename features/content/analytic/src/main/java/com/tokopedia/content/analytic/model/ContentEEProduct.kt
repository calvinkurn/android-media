package com.tokopedia.content.analytic.model

/**
 * Created By : Jonathan Darwin on January 23, 2024
 */
data class ContentEEProduct(
    val itemId: String,
    val itemName: String,
    val itemBrand: String,
    val itemCategory: String,
    val itemVariant: String,
    val price: String,
    val index: String,
    val customFields: Map<String, String>,
)

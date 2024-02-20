package com.tokopedia.content.analytic.model

/**
 * Created By : Jonathan Darwin on January 24, 2024
 */
sealed interface ContentEnhanceEcommerce {

    data class Product(
        val itemId: String,
        val itemName: String,
        val itemBrand: String,
        val itemCategory: String,
        val itemVariant: String,
        val price: String,
        val index: String,
        val customFields: Map<String, String>,
    ) : ContentEnhanceEcommerce

    data class Promotion(
        val itemId: String,
        val itemName: String,
        val creativeSlot: String,
        val creativeName: String,
    ) : ContentEnhanceEcommerce
}

internal fun List<ContentEnhanceEcommerce>.isEEProduct(): Boolean {
    return isNotEmpty() && first() is ContentEnhanceEcommerce.Product
}

internal fun List<ContentEnhanceEcommerce>.isEEPromotion(): Boolean {
    return isNotEmpty() && first() is ContentEnhanceEcommerce.Promotion
}

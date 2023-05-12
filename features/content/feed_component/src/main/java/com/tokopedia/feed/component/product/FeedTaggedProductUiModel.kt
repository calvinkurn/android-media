package com.tokopedia.feed.component.product

/**
 * Created by meyta.taliti on 11/05/23.
 */
data class FeedTaggedProductUiModel(
    val id: String,
    val shopId: String,
    val title: String,
    val imageUrl: String,
    val price: Price,
    val appLink: String,
) {

    data class DiscountedPrice(
        val discount: Int,
        val originalPrice: String,
        val price: String,
    ): Price()

    data class NormalPrice(
        val price: String
    ): Price()

    sealed class Price
}

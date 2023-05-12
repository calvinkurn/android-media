package com.tokopedia.feed.component.product

/**
 * Created by meyta.taliti on 11/05/23.
 */
data class FeedTaggedProductUiModel(
    val id: String,
    val shop: Shop,
    val title: String,
    val imageUrl: String,
    val price: Price,
    val appLink: String,
) {
    data class Shop(
        val id: String,
        val name: String,
    )
    data class DiscountedPrice(
        val discount: Int,
        val originalFormattedPrice: String,
        val formattedPrice: String,
        val price: Double
    ): Price()

    data class NormalPrice(
        val formattedPrice: String,
        val price: Double
    ): Price()

    sealed class Price

    val finalPrice: Double get() {
        return when(val price = this.price) {
            is DiscountedPrice -> price.price
            is NormalPrice -> price.price
        }
    }
}

package com.tokopedia.shop.home.view.model

sealed class ShopHomeProductCarouselItem {
    data class ProductCarouselWithMainBanner(
        val title: String,
        val tabs: List<ShopHomeProductCarouselUiModel.Tab>,
        val mainBanner: MainBanner,
        val showProductInfo: Boolean
    ) : ShopHomeProductCarouselItem()

    data class ProductCarousel(
        val title: String,
        val verticalBanner: ProductCarouselVerticalBanner,
        val items: List<ShopHomeProductCarouselItemType>,
        val showProductInfo: Boolean,
        val showChevron: Boolean,
        val chevronAppLink: String
    ) : ShopHomeProductCarouselItem()
}

data class MainBanner(
    val componentName: String,
    val componentType: String,
    val ratio: String,
    val imageUrl: String,
    val appLink: String
)

data class ProductCarouselVerticalBanner(val imageUrl: String, val appLink: String)

package com.tokopedia.search.result.product.inspirationcarousel

interface InspirationCarouselView {
    val queryKey: String

    fun trackInspirationCarouselChipsClicked(option: InspirationCarouselDataView.Option)

    fun trackEventImpressionInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product)

    fun trackEventImpressionInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product)

    fun trackEventImpressionInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product)

    fun trackEventClickInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product)

    fun trackEventClickInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product)

    fun trackEventClickInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product)

    fun openLink(applink: String, url: String)
}

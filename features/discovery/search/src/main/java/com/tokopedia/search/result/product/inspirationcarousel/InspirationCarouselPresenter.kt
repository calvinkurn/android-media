package com.tokopedia.search.result.product.inspirationcarousel

interface InspirationCarouselPresenter {
    fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselChipsClick(
        adapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>
    )
}

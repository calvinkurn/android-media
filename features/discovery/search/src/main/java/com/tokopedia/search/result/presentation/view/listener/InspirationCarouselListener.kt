package com.tokopedia.search.result.presentation.view.listener


import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView

interface InspirationCarouselListener {

    fun onInspirationCarouselListProductClicked(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option)

    fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselDataView.Option.Product)

    fun onImpressedInspirationCarouselInfoProduct(product: InspirationCarouselDataView.Option.Product)

    fun onImpressedInspirationCarouselListProduct(product: InspirationCarouselDataView.Option.Product)

    fun onImpressedInspirationCarouselGridProduct(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselGridProductClicked(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselGridBannerClicked(option: InspirationCarouselDataView.Option)

    fun onInspirationCarouselChipsProductClicked(product: InspirationCarouselDataView.Option.Product)

    fun onImpressedInspirationCarouselChipsProduct(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselChipsSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option)

    fun onInspirationCarouselChipsClicked(
            inspirationCarouselAdapterPosition: Int,
            inspirationCarouselViewModel: InspirationCarouselDataView,
            inspirationCarouselOption: InspirationCarouselDataView.Option,
    )
}
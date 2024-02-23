package com.tokopedia.search.result.product.inspirationcarousel

interface InspirationCarouselListener {

    fun onInspirationCarouselListProductImpressed(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselOptionImpressed1Px(
        option: InspirationCarouselDataView.Option,
        adapterPosition: Int
    )

    fun onInspirationCarouselListProductClicked(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option)

    fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselGridProductImpressed(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselGridProductImpressed1Px(
        product: InspirationCarouselDataView.Option.Product,
        optionAdapterPosition: Int,
    )

    fun onInspirationCarouselGridProductClicked(
        product: InspirationCarouselDataView.Option.Product,
        optionAdapterPosition: Int,
    )

    fun onInspirationCarouselGridBannerClicked(option: InspirationCarouselDataView.Option)

    fun onInspirationCarouselChipsProductClicked(
        product: InspirationCarouselDataView.Option.Product,
        optionAdapterPosition: Int
    )

    fun onImpressedInspirationCarouselChipsProduct(
        product: InspirationCarouselDataView.Option.Product,
        optionAdapterPosition: Int,
    )

    fun onInspirationCarouselChipsSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option)

    fun onInspirationCarouselChipsClicked(
        inspirationCarouselAdapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        inspirationCarouselOption: InspirationCarouselDataView.Option,
    )
}

package com.tokopedia.search.result.presentation.view.listener


import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel

interface InspirationCarouselListener {

    fun onInspirationCarouselListProductClicked(product: InspirationCarouselViewModel.Option.Product)

    fun onInspirationCarouselSeeAllClicked(inspirationCarouselViewModelOption: InspirationCarouselViewModel.Option)

    fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselViewModel.Option.Product)
}
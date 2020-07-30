package com.tokopedia.search.result.presentation.view.listener


import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel

interface InspirationCarouselListener {

    fun onInspirationCarouselProductClicked(product: InspirationCarouselViewModel.Option.Product)

    fun onInspirationCarouselSeeAllClicked(inspirationCarouselViewModelOption: InspirationCarouselViewModel.Option)
}
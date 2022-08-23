package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import androidx.recyclerview.widget.RecyclerView

interface CarouselListener {
    val carouselRecycledViewPool: RecyclerView.RecycledViewPool?
    fun onCarouselSeeAllClick(data: CarouselDataView)
    fun onCarouselItemClick(data: CarouselDataView.Product)
    fun onCarouselItemImpressed(data: CarouselDataView.Product)
}
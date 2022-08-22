package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import androidx.recyclerview.widget.RecyclerView

class CarouselListenerDelegate: CarouselListener {
    override val carouselRecycledViewPool: RecyclerView.RecycledViewPool
        get() = RecyclerView.RecycledViewPool()
}
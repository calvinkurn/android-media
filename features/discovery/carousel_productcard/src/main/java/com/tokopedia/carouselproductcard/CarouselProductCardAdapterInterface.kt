package com.tokopedia.carouselproductcard

import androidx.recyclerview.widget.RecyclerView

internal interface CarouselProductCardAdapterInterface {

    fun asRecyclerViewAdapter(): RecyclerView.Adapter<*>

    fun submitCarouselProductCardModelList(list: List<CarouselProductCardModel>?)
}
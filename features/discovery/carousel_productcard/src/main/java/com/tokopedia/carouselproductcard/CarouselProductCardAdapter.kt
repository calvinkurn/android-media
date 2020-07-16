package com.tokopedia.carouselproductcard

import androidx.recyclerview.widget.RecyclerView

internal interface CarouselProductCardAdapter {

    fun asRecyclerViewAdapter(): RecyclerView.Adapter<*>

    fun submitCarouselProductCardModelList(list: List<BaseCarouselCardModel>?)
}
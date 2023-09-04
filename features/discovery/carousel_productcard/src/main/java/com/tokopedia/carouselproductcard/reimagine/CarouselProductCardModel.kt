package com.tokopedia.carouselproductcard.reimagine

import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.Visitable

data class CarouselProductCardModel(
    val itemList: List<Visitable<CarouselProductCardTypeFactory>> = listOf(),
    val scrollToPosition: Int = 0,
    val recycledViewPool: RecycledViewPool = RecycledViewPool(),
)

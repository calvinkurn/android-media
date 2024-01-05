package com.tokopedia.carouselproductcard.reimagine.viewallcard

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardTypeFactory
import com.tokopedia.viewallcard.ViewAllCard.Companion.MODE_NORMAL

data class CarouselProductCardViewAllCardModel(
    val title: String = "",
    val description: String = "",
    val titleIsInteger: Boolean = false,
    val viewAllCardMode: Int = MODE_NORMAL,
    val ctaText: String = "",
    val onClick: () -> Unit = { },
): Visitable<CarouselProductCardTypeFactory> {

    override fun type(typeFactory: CarouselProductCardTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}

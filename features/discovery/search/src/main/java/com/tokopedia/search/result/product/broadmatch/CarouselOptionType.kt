package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

sealed class CarouselOptionType {

    companion object {
        fun of(type: String, option: InspirationCarouselDataView.Option): CarouselOptionType =
            if (type == TYPE_INSPIRATION_CAROUSEL_KEYWORD) BroadMatch
            else DynamicCarouselOption(option)
    }
}

object BroadMatch : CarouselOptionType()

class DynamicCarouselOption(
    val option: InspirationCarouselDataView.Option
) : CarouselOptionType()

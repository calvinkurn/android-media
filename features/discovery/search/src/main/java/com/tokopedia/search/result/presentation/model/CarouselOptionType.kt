package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

sealed class CarouselOptionType

object BroadMatch: CarouselOptionType()

class DynamicCarouselOption(
    val option: InspirationCarouselDataView.Option
): CarouselOptionType()
package com.tokopedia.search.result.presentation.model

sealed class CarouselOptionType

object BroadMatch: CarouselOptionType()

class DynamicCarouselOption(
    val option: InspirationCarouselDataView.Option
): CarouselOptionType()
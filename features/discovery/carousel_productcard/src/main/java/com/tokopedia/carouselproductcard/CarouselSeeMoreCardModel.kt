package com.tokopedia.carouselproductcard

internal data class CarouselSeeMoreCardModel(
        val applink: String,
        var carouselProductCardListenerInfo: CarouselProductCardListenerInfo
) : BaseCarouselCardModel{
    override fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselSeeMoreCardModel && applink == newItem.applink
    }

    override fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselSeeMoreCardModel && applink == newItem.applink
    }

    fun getOnSeeMoreClickListener() = carouselProductCardListenerInfo.onSeeMoreClickListener
}
package com.tokopedia.video_widget.carousel

interface AutoPlayReceiverDecider {

    fun getEligibleAutoPlayReceivers(
        visibleCards: List<AutoPlayModel>,
        itemCount: Int,
        maxAutoPlay: Int
    ): List<CarouselVideoPlayerReceiver>
}
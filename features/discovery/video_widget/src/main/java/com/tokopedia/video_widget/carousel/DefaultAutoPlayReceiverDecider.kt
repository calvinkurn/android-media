package com.tokopedia.video_widget.carousel

import android.view.View

class DefaultAutoPlayReceiverDecider : AutoPlayReceiverDecider {

    override fun getEligibleAutoPlayReceivers(
        visibleCards: List<AutoPlayModel>,
        itemCount: Int,
        maxAutoPlay: Int
    ): List<CarouselVideoPlayerReceiver> {
        val isEndOfList = visibleCards.find { it.position == itemCount - 1 } != null
        val playerReceivers: List<CarouselVideoPlayerReceiver> = visibleCards.mapNotNull {
            getEligibleAutoPlayReceiver(it.view)
        }

        return if (!isEndOfList) {
            playerReceivers.take(maxAutoPlay)
        } else {
            playerReceivers.takeLast(maxAutoPlay)
        }
    }

    private fun getEligibleAutoPlayReceiver(view: View): CarouselVideoPlayerReceiver? {
        return if (view is CarouselVideoPlayerReceiver && view.isPlayable()) view
        else null
    }
}
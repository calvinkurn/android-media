package com.tokopedia.play.widget.ui.autoplay

import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver

/**
 * Created by jegul on 22/10/20
 */
class DefaultAutoPlayReceiverDecider : AutoPlayReceiverDecider {

    override fun getEligibleAutoPlayReceivers(
        visibleCards: List<AutoPlayModel>,
        itemCount: Int,
        maxAutoPlay: Int
    ): List<PlayVideoPlayerReceiver> {
        val isStartOfListVisible = visibleCards.any { it.position == 0 }
        val isEndOfListVisible = visibleCards.any { it.position == itemCount - 1 }
        val playerReceivers: List<PlayVideoPlayerReceiver> = visibleCards.mapNotNull {
            if (it.card is PlayVideoPlayerReceiver && it.card.isPlayable()) {
                it.card
            } else {
                null
            }
        }

        return if (!isStartOfListVisible && isEndOfListVisible) {
            playerReceivers.takeLast(maxAutoPlay)
        } else {
            playerReceivers.take(maxAutoPlay)
        }
    }
}

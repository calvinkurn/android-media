package com.tokopedia.play.widget.ui.autoplay

import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver

/**
 * Created by jegul on 22/10/20
 */
interface AutoPlayReceiverDecider {

    fun getEligibleAutoPlayReceivers(
            visibleCards: List<AutoPlayModel>,
            itemCount: Int,
            maxAutoPlay: Int
    ): List<PlayVideoPlayerReceiver>
}
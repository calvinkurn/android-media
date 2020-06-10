package com.tokopedia.play.ui.videocontrol.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 16/04/20
 */
sealed class VideoControlInteractionEvent : ComponentEvent {

    object VideoScrubStarted : VideoControlInteractionEvent()
    object VideoScrubEnded : VideoControlInteractionEvent()
}
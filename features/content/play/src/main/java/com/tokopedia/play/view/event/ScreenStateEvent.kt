package com.tokopedia.play.view.event

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 02/12/19
 */
sealed class ScreenStateEvent : ComponentEvent {

    data class Play(val exoPlayer: ExoPlayer) : ScreenStateEvent()
    data class SetPinned(val author: String, val message: String) : ScreenStateEvent()
}
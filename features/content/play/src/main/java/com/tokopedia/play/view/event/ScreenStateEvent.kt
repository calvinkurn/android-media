package com.tokopedia.play.view.event

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.ui.chatlist.model.PlayChat

/**
 * Created by jegul on 02/12/19
 */
sealed class ScreenStateEvent : ComponentEvent {

    data class Play(val exoPlayer: ExoPlayer) : ScreenStateEvent()
    data class SetPinned(val author: String, val message: String) : ScreenStateEvent()
    data class Chat(val chat: PlayChat) : ScreenStateEvent()

    object Destroy : ScreenStateEvent()
}
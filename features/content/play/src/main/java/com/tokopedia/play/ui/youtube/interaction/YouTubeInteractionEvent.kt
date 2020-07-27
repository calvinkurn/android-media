package com.tokopedia.play.ui.youtube.interaction

import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created by jegul on 28/04/20
 */
sealed class YouTubeInteractionEvent : ComponentEvent {

    data class YouTubeVideoStateChanged(val videoState: PlayVideoState) : YouTubeInteractionEvent()
    object EnterFullscreenClicked : YouTubeInteractionEvent()
    object ExitFullscreenClicked :YouTubeInteractionEvent()
}
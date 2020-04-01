package com.tokopedia.play.ui.endliveinfo.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 14/01/20
 */
sealed class EndLiveInfoInteractionEvent : ComponentEvent {

    data class ButtonActionClicked(val buttonUrl: String) : EndLiveInfoInteractionEvent()
}
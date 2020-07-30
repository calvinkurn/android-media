package com.tokopedia.play.ui.playbutton.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 10/12/19
 */
sealed class PlayButtonInteractionEvent : ComponentEvent {

    object PlayClicked : PlayButtonInteractionEvent()
}
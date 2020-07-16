package com.tokopedia.play.ui.closebutton.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 05/05/20
 */
sealed class CloseButtonInteractionEvent : ComponentEvent {

    object OnClicked : CloseButtonInteractionEvent()
}
package com.tokopedia.play.ui.immersivebox.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 13/12/19
 */
sealed class ImmersiveBoxInteractionEvent : ComponentEvent {

    object BoxClicked : ImmersiveBoxInteractionEvent()
}
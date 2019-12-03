package com.tokopedia.play.ui.pinned.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 03/12/19
 */
sealed class PinnedInteractionEvent : ComponentEvent {

    object ActionClicked : PinnedInteractionEvent()
}
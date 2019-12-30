package com.tokopedia.play.ui.pinned.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 30/12/19
 */
sealed class PinnedInteractionEvent : ComponentEvent {

    data class PinnedActionClicked(val applink: String) : PinnedInteractionEvent()
}
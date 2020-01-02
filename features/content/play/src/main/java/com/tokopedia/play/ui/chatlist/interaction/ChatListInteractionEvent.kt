package com.tokopedia.play.ui.chatlist.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 02/01/20
 */
sealed class ChatListInteractionEvent : ComponentEvent {

    data class PositionYCalculated(val yPos: Int) : ChatListInteractionEvent()
}
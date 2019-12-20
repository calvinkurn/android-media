package com.tokopedia.play.ui.sendchat.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 02/12/19
 */
sealed class SendChatInteractionEvent : ComponentEvent {

    object FormClicked : SendChatInteractionEvent()
    object SendClicked : SendChatInteractionEvent()
}
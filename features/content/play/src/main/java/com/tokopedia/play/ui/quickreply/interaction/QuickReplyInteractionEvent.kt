package com.tokopedia.play.ui.quickreply.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 13/12/19
 */
sealed class QuickReplyInteractionEvent : ComponentEvent {

    data class ReplyClicked(val replyString: String) : QuickReplyInteractionEvent()
}
package com.tokopedia.chatbot.data.rating

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * @author by yfsx on 14/05/18.
 */

class ChatRatingViewModel constructor(msgId: String = "",
                          fromUid: String = "",
                          from: String = "",
                          fromRole: String = "",
                          message: String = "",
                          attachmentId: String = "",
                          attachmentType: String = "",
                          replyTime: String = "",
                          var ratingStatus: Int = 0,
                          val replyTimeNano: Long = 0,
                          var quickReplies: List<QuickReplyViewModel> = ArrayList(),
                          source: String = "") : BaseChatUiModel(msgId, fromUid, from,
        fromRole, attachmentId, attachmentType, replyTime, message, source)
        , Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val RATING_NONE = 0
        const val RATING_GOOD = 1
        const val RATING_BAD = -1
    }
}

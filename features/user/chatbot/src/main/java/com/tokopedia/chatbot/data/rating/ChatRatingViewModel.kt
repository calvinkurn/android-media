package com.tokopedia.chatbot.data.rating

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * @author by yfsx on 14/05/18.
 */

class ChatRatingViewModel(msgId: String,
                          fromUid: String,
                          from: String,
                          fromRole: String,
                          message: String,
                          attachmentId: String,
                          attachmentType: String,
                          replyTime: String,
                          var ratingStatus: Int,
                          val replyTimeNano: Long) : BaseChatViewModel(msgId, fromUid, from,
        fromRole, attachmentId, attachmentType, replyTime, message)
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

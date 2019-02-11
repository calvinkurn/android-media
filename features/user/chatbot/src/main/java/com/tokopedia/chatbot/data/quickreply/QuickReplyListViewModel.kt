package com.tokopedia.chatbot.data.quickreply

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by yfsx on 08/05/18.
 */

class QuickReplyListViewModel(msgId: String = "",
                              fromUid: String = "",
                              from: String = "",
                              fromRole: String = "",
                              message: String = "",
                              attachmentId: String= "",
                              attachmentType: String = "",
                              replyTime: String = "",
                              var quickReplies: List<QuickReplyViewModel> = ArrayList())
    : BaseChatViewModel(msgId, fromUid, from, fromRole, attachmentId,
        attachmentType, replyTime, message), Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun EMPTY(): QuickReplyListViewModel {
        return QuickReplyListViewModel(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ArrayList())
    }

}

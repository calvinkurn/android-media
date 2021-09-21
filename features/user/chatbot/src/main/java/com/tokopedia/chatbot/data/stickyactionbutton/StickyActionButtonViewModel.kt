package com.tokopedia.chatbot.data.stickyactionbutton

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

class StickyActionButtonViewModel(messageId: String = "",
                                  fromUid: String = "",
                                  from: String = "",
                                  fromRole: String = "",
                                  attachmentId: String = "",
                                  attachmentType: String = "",
                                  replyTime: String = "",
                                  message: String = "",
                                  var stickyActionButton: List<StickyActionButtonPojo.StickedButtonAction?>?,
                                  source: String = ""
) : BaseChatViewModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source)
        , Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}

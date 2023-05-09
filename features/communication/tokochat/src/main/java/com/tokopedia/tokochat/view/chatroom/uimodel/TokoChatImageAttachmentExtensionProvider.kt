package com.tokopedia.tokochat.view.chatroom.uimodel

import com.gojek.conversations.extensions.ConversationsContext
import com.gojek.conversations.extensions.ConversationsExtension
import com.gojek.conversations.extensions.ConversationsExtensionProvider
import com.tokopedia.tokochat.util.TokoChatValueUtil
import javax.inject.Inject

class TokoChatImageAttachmentExtensionProvider @Inject constructor() : ConversationsExtensionProvider {
    override fun getConversationsExtensionInstance(
        context: ConversationsContext
    ): ConversationsExtension {
        return TokoChatImageAttachmentExtension(context)
    }

    override fun getExtensionId(): String = TokoChatValueUtil.PICTURE
}

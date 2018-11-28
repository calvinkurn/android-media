package com.tokopedia.chatbot.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl

/**
 * @author by nisie on 27/11/18.
 */

open class ChatbotTypeFactoryImpl() :
        BaseChatTypeFactoryImpl(),
        ChatbotTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InboxTalkItemViewHolder.LAYOUT -> InboxTalkItemViewHolder(parent, talkItemListener,
                    talkCommentItemListener, talkAttachmentItemClickListener, talkCommentLoadMoreListener)
            EmptyInboxTalkViewHolder.LAYOUT -> EmptyInboxTalkViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}
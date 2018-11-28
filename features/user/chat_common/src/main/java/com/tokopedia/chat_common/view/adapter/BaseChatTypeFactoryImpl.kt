package com.tokopedia.chat_common.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.TypingChatModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageAnnouncementViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.MessageViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.TypingChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener

/**
 * @author by nisie on 27/11/18.
 */

open class BaseChatTypeFactoryImpl(private val imageAnnouncementListener: ImageAnnouncementListener,
                                   private val chatLinkHandlerListener: ChatLinkHandlerListener,
                                   private val imageUploadListener : ImageUploadListener) :
        BaseAdapterTypeFactory(),
        BaseChatTypeFactory {

    override fun type(messageViewModel: MessageViewModel): Int {
        return MessageViewHolder.LAYOUT
    }

    override fun type(typingViewModel: TypingChatModel): Int {
        return TypingChatViewHolder.LAYOUT
    }

    override fun type(imageAnnouncementViewModel: ImageAnnouncementViewModel): Int {
        return ImageAnnouncementViewHolder.LAYOUT
    }

    override fun type(imageUploadViewModel: ImageUploadViewModel): Int {
        return ImageUploadViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TypingChatViewHolder.LAYOUT -> TypingChatViewHolder(parent)
            MessageViewHolder.LAYOUT -> MessageViewHolder(parent, chatLinkHandlerListener)
            ImageAnnouncementViewHolder.LAYOUT -> ImageAnnouncementViewHolder(parent, imageAnnouncementListener)
            ImageUploadViewHolder.LAYOUT -> ImageUploadViewHolder(parent, imageUploadListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
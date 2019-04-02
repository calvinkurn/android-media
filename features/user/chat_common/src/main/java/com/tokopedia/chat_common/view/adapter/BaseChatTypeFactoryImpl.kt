package com.tokopedia.chat_common.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.view.adapter.viewholder.*
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener

/**
 * @author by nisie on 27/11/18.
 */

open class BaseChatTypeFactoryImpl(private val imageAnnouncementListener: ImageAnnouncementListener,
                                   private val chatLinkHandlerListener: ChatLinkHandlerListener,
                                   private val imageUploadListener : ImageUploadListener,
                                   private val productAttachmentListener : ProductAttachmentListener) :
        BaseAdapterTypeFactory(),
        BaseChatTypeFactory {
    override fun type(productAttachmentViewModel: ProductAttachmentViewModel): Int {
        return ProductAttachmentViewHolder.LAYOUT;
    }

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

    override fun type(fallbackAttachmentViewModel: FallbackAttachmentViewModel): Int {
        return FallbackAttachmentViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TypingChatViewHolder.LAYOUT -> TypingChatViewHolder(parent)
            MessageViewHolder.LAYOUT -> MessageViewHolder(parent, chatLinkHandlerListener)
            ImageAnnouncementViewHolder.LAYOUT -> ImageAnnouncementViewHolder(parent, imageAnnouncementListener)
            ImageUploadViewHolder.LAYOUT -> ImageUploadViewHolder(parent, imageUploadListener)
            FallbackAttachmentViewHolder.LAYOUT -> FallbackAttachmentViewHolder(parent, chatLinkHandlerListener)
            ProductAttachmentViewHolder.LAYOUT -> ProductAttachmentViewHolder(parent, productAttachmentListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
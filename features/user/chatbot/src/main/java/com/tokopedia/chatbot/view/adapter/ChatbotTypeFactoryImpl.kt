package com.tokopedia.chatbot.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel

/**
 * @author by nisie on 27/11/18.
 */

open class ChatbotTypeFactoryImpl(imageAnnouncementListener: ImageAnnouncementListener,
                                  chatLinkHandlerListener: ChatLinkHandlerListener,
                                  imageUploadListener : ImageUploadListener,
                                  productAttachmentListener : ProductAttachmentListener) :
        BaseChatTypeFactoryImpl(imageAnnouncementListener, chatLinkHandlerListener,
                imageUploadListener, productAttachmentListener),
        ChatbotTypeFactory {

    override fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int {
        return AttachedInvoiceSentViewHolder.LAYOUT;
    }

    override fun type(attachInvoiceSelectionViewModel: AttachInvoiceSelectionViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(quickReplyListViewModel: QuickReplyListViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(chatRating: ChatRatingViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(chatBubble: ChatActionSelectionBubbleViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InboxTalkItemViewHolder.LAYOUT -> InboxTalkItemViewHolder(parent, talkItemListener,
                    talkCommentItemListener, talkAttachmentItemClickListener, talkCommentLoadMoreListener)
            EmptyInboxTalkViewHolder.LAYOUT -> EmptyInboxTalkViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}
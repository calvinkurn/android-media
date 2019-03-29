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
import com.tokopedia.chatbot.view.adapter.viewholder.*
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener

/**
 * @author by nisie on 27/11/18.
 */

open class ChatbotTypeFactoryImpl(imageAnnouncementListener: ImageAnnouncementListener,
                                  private val chatLinkHandlerListener: ChatLinkHandlerListener,
                                  imageUploadListener: ImageUploadListener,
                                  productAttachmentListener: ProductAttachmentListener,
                                  private val attachedInvoiceSelectionListener:
                                  AttachedInvoiceSelectionListener,
                                  private val chatRatingListener: ChatRatingListener,
                                  private val chatActionListBubbleListener: ChatActionListBubbleListener) :
        BaseChatTypeFactoryImpl(imageAnnouncementListener, chatLinkHandlerListener,
                imageUploadListener, productAttachmentListener),
        ChatbotTypeFactory {

    override fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int {
        return AttachedInvoiceSentViewHolder.LAYOUT
    }

    override fun type(attachInvoiceSelectionViewModel: AttachInvoiceSelectionViewModel): Int {
        return AttachedInvoiceSelectionViewHolder.LAYOUT
    }

    override fun type(quickReplyListViewModel: QuickReplyListViewModel): Int {
        return QuickReplyViewHolder.LAYOUT
    }

    override fun type(chatRating: ChatRatingViewModel): Int {
        return ChatRatingViewHolder.LAYOUT
    }

    override fun type(chatBubble: ChatActionSelectionBubbleViewModel): Int {
        return ChatActionListBubbleViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AttachedInvoiceSentViewHolder.LAYOUT -> AttachedInvoiceSentViewHolder(parent)
            AttachedInvoiceSelectionViewHolder.LAYOUT -> AttachedInvoiceSelectionViewHolder(parent, attachedInvoiceSelectionListener)
            QuickReplyViewHolder.LAYOUT -> QuickReplyViewHolder(parent, chatLinkHandlerListener)
            ChatRatingViewHolder.LAYOUT -> ChatRatingViewHolder(parent, chatLinkHandlerListener, chatRatingListener)
            ChatActionListBubbleViewHolder.LAYOUT -> ChatActionListBubbleViewHolder(parent, chatActionListBubbleListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
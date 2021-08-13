package com.tokopedia.chatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.*
import com.tokopedia.chatbot.view.adapter.viewholder.chatbubble.CustomChatbotMessageViewHolder
import com.tokopedia.chatbot.view.adapter.viewholder.chatbubble.LeftChatMessageViewHolder
import com.tokopedia.chatbot.view.adapter.viewholder.chatbubble.RightChatMessageViewHolder
import com.tokopedia.chatbot.view.adapter.viewholder.listener.*

/**
 * @author by nisie on 27/11/18.
 */

open class ChatbotTypeFactoryImpl(imageAnnouncementListener: ImageAnnouncementListener,
                                  private val chatLinkHandlerListener: ChatLinkHandlerListener,
                                  private val imageUploadListener: ImageUploadListener,
                                  productAttachmentListener: ProductAttachmentListener,
                                  private val attachedInvoiceSelectionListener:
                                  AttachedInvoiceSelectionListener,
                                  private val chatRatingListener: ChatRatingListener,
                                  private val chatActionListBubbleListener: ChatActionListBubbleListener,
                                  private val chatOptionListListener: ChatOptionListListener,
                                  private val csatOptionListListener: CsatOptionListListener,
                                  private val actionButtonClickListener: StickyActionButtonClickListener) :
        BaseChatTypeFactoryImpl(imageAnnouncementListener, chatLinkHandlerListener,
                imageUploadListener, productAttachmentListener),
        ChatbotTypeFactory {

    override fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int {
        if (position < 0 || position >= visitables.size) {
            return HideViewHolder.LAYOUT
        }
        val chat = visitables[position]
        return if (chat is MessageViewModel) {
            if (chat.isSender) {
                CustomChatbotMessageViewHolder.TYPE_RIGHT
            } else {
                CustomChatbotMessageViewHolder.TYPE_LEFT
            }
        } else {
            default
        }
    }

    override fun createViewHolder(
            parent: ViewGroup,
            type: Int,
            chatbotAdapterListener: ChatbotAdapterListener
    ): AbstractViewHolder<*> {
        val layoutRes = when (type) {
            CustomChatbotMessageViewHolder.TYPE_LEFT -> LeftChatMessageViewHolder.LAYOUT
            CustomChatbotMessageViewHolder.TYPE_RIGHT -> RightChatMessageViewHolder.LAYOUT
            else -> type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view, layoutRes, chatbotAdapterListener)
    }

    private fun createViewHolder(parent: View, type: Int, chatbotAdapterListener: ChatbotAdapterListener): AbstractViewHolder<*> {
        return when (type) {
            LeftChatMessageViewHolder.LAYOUT -> LeftChatMessageViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener)
            CsatOptionListViewHolder.LAYOUT -> CsatOptionListViewHolder(parent, csatOptionListListener, chatLinkHandlerListener, chatbotAdapterListener)
            ChatHelpfullQuestionViewHolder.LAYOUT -> ChatHelpfullQuestionViewHolder(parent, chatOptionListListener, chatLinkHandlerListener, chatbotAdapterListener)
            QuickReplyViewHolder.LAYOUT -> QuickReplyViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener)
            ChatbotFallbackAttachmentViewHolder.LAYOUT -> ChatbotFallbackAttachmentViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener)
            StickyActionButtonViewHolder.LAYOUT -> StickyActionButtonViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener, actionButtonClickListener)
            else -> createViewHolder(parent, type)
        }
    }

    override fun type(imageUploadViewModel: ImageUploadViewModel): Int {
        return ChatbotImageUploadViewHolder.LAYOUT
    }

    override fun type(stickyActionButtonViewModel: StickyActionButtonViewModel): Int {
        return StickyActionButtonViewHolder.LAYOUT
    }

    override fun type(chatSepratorViewModel: ChatSepratorViewModel): Int {
        return ChatbotLiveChatSeparatorViewHolder.LAYOUT
    }

    override fun type(connectionDividerViewModel: ConnectionDividerViewModel): Int {
        return ConnectionDividerViewHolder.LAYOUT
    }

    override fun type(helpFullQuestionsViewModel: HelpFullQuestionsViewModel): Int {
        return ChatHelpfullQuestionViewHolder.LAYOUT
    }

    override fun type(csatOptionsViewModel: CsatOptionsViewModel): Int {
        return CsatOptionListViewHolder.LAYOUT
    }

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

    override fun type(fallbackAttachmentViewModel: FallbackAttachmentViewModel): Int {
        return ChatbotFallbackAttachmentViewHolder.LAYOUT
    }

    override fun type(typingViewModel: TypingChatModel): Int {
        return ChatBotTypingChatViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ChatBotTypingChatViewHolder.LAYOUT -> ChatBotTypingChatViewHolder(parent)
            RightChatMessageViewHolder.LAYOUT -> RightChatMessageViewHolder(parent, chatLinkHandlerListener)
            ConnectionDividerViewHolder.LAYOUT -> ConnectionDividerViewHolder(parent)
            ChatbotLiveChatSeparatorViewHolder.LAYOUT -> ChatbotLiveChatSeparatorViewHolder(parent)
            AttachedInvoiceSentViewHolder.LAYOUT -> AttachedInvoiceSentViewHolder(parent)
            AttachedInvoiceSelectionViewHolder.LAYOUT -> AttachedInvoiceSelectionViewHolder(parent, attachedInvoiceSelectionListener)
            ChatRatingViewHolder.LAYOUT -> ChatRatingViewHolder(parent, chatLinkHandlerListener, chatRatingListener)
            ChatActionListBubbleViewHolder.LAYOUT -> ChatActionListBubbleViewHolder(parent, chatActionListBubbleListener, chatLinkHandlerListener)
            ChatbotImageUploadViewHolder.LAYOUT -> ChatbotImageUploadViewHolder(parent, imageUploadListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
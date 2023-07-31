package com.tokopedia.chatbot.chatbot2.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.TypingChatModel
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatActionListBubbleViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatBotTypingChatViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatHelpfullQuestionViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatRatingViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatbotFallbackAttachmentViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatbotImageUploadViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatbotLiveChatSeparatorViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.ChatbotVideoUploadViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.CsatOptionListViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.QuickReplyViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.StickyActionButtonViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatbubble.CustomChatbotMessageViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatbubble.LeftChatMessageUnifyViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatbubble.RightChatMessageUnifyViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment.DynamicAttachmentTextViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment.DynamicOwocInvoiceViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment.DynamicStickyButtonViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.invoice.AttachedInvoiceSelectionViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.invoice.AttachedInvoiceSentViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatOptionListListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotOwocListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.CsatOptionListListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.DynamicStickyButtonListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.VideoUploadListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicAttachmentTextUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicOwocInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.stickyactionbutton.StickyActionButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 27/11/18.
 */

open class ChatbotTypeFactoryImpl(
    imageAnnouncementListener: ImageAnnouncementListener,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    private val imageUploadListener: ImageUploadListener,
    productAttachmentListener: ProductAttachmentListener,
    private val attachedInvoiceSelectionListener:
        AttachedInvoiceSelectionListener,
    private val chatRatingListener: ChatRatingListener,
    private val chatActionListBubbleListener: ChatActionListBubbleListener,
    private val chatOptionListListener: ChatOptionListListener,
    private val csatOptionListListener: CsatOptionListListener,
    private val actionButtonClickListener: StickyActionButtonClickListener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener,
    private val videoUploadListener: VideoUploadListener,
    private val dynamicStickyButtonListener: DynamicStickyButtonListener,
    private val dynamicOwocListener: ChatbotOwocListener,
    private val userSession: UserSessionInterface
) :
    BaseChatTypeFactoryImpl(
        imageAnnouncementListener,
        chatLinkHandlerListener,
        imageUploadListener,
        productAttachmentListener
    ),
    ChatbotTypeFactory {

    override fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int {
        if (position < 0 || position >= visitables.size) {
            return HideViewHolder.LAYOUT
        }
        val chat = visitables[position]
        return if (chat is MessageUiModel) {
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
            CustomChatbotMessageViewHolder.TYPE_LEFT -> LeftChatMessageUnifyViewHolder.LAYOUT
            CustomChatbotMessageViewHolder.TYPE_RIGHT -> RightChatMessageUnifyViewHolder.LAYOUT
            else -> type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view, layoutRes, chatbotAdapterListener)
    }

    private fun createViewHolder(parent: View, type: Int, chatbotAdapterListener: ChatbotAdapterListener): AbstractViewHolder<*> {
        return when (type) {
            LeftChatMessageUnifyViewHolder.LAYOUT -> LeftChatMessageUnifyViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener, replyBubbleListener, userSession)
            CsatOptionListViewHolder.LAYOUT -> CsatOptionListViewHolder(parent, csatOptionListListener, chatLinkHandlerListener, chatbotAdapterListener)
            ChatHelpfullQuestionViewHolder.LAYOUT -> ChatHelpfullQuestionViewHolder(parent, chatOptionListListener, chatLinkHandlerListener, chatbotAdapterListener)
            QuickReplyViewHolder.LAYOUT -> QuickReplyViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener)
            ChatbotFallbackAttachmentViewHolder.LAYOUT -> ChatbotFallbackAttachmentViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener)
            StickyActionButtonViewHolder.LAYOUT -> StickyActionButtonViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener, actionButtonClickListener)
            ChatRatingViewHolder.LAYOUT -> ChatRatingViewHolder(parent, chatLinkHandlerListener, chatbotAdapterListener, chatRatingListener)
            DynamicStickyButtonViewHolder.LAYOUT -> DynamicStickyButtonViewHolder(
                parent,
                chatLinkHandlerListener,
                dynamicStickyButtonListener
            )
            DynamicOwocInvoiceViewHolder.LAYOUT -> DynamicOwocInvoiceViewHolder(
                parent,
                chatLinkHandlerListener,
                dynamicOwocListener
            )
            else -> createViewHolder(parent, type)
        }
    }

    override fun type(imageUploadUiModel: ImageUploadUiModel): Int {
        return ChatbotImageUploadViewHolder.LAYOUT
    }

    override fun type(stickyActionButtonUiModel: StickyActionButtonUiModel): Int {
        return StickyActionButtonViewHolder.LAYOUT
    }

    override fun type(attachInvoiceSentUiModel: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel): Int {
        return AttachedInvoiceSentViewHolder.LAYOUT
    }

    override fun type(dynamicStickyButtonUiModel: DynamicStickyButtonUiModel): Int {
        return DynamicStickyButtonViewHolder.LAYOUT
    }

    override fun type(dynamicAttachmentTextUiModel: DynamicAttachmentTextUiModel): Int {
        return DynamicAttachmentTextViewHolder.LAYOUT
    }

    override fun type(dynamicOwocInvoiceUiModel: DynamicOwocInvoiceUiModel): Int {
        return DynamicOwocInvoiceViewHolder.LAYOUT
    }

    override fun type(chatSepratorUiModel: ChatSepratorUiModel): Int {
        return ChatbotLiveChatSeparatorViewHolder.LAYOUT
    }

    override fun type(helpFullQuestionsUiModel: HelpFullQuestionsUiModel): Int {
        return ChatHelpfullQuestionViewHolder.LAYOUT
    }

    override fun type(csatOptionsUiModel: CsatOptionsUiModel): Int {
        return CsatOptionListViewHolder.LAYOUT
    }

    override fun type(attachInvoiceSelectionUiModel: AttachInvoiceSelectionUiModel): Int {
        return AttachedInvoiceSelectionViewHolder.LAYOUT
    }

    override fun type(quickReplyListUiModel: QuickReplyListUiModel): Int {
        return QuickReplyViewHolder.LAYOUT
    }

    override fun type(chatRating: ChatRatingUiModel): Int {
        return ChatRatingViewHolder.LAYOUT
    }

    override fun type(chatBubble: ChatActionSelectionBubbleUiModel): Int {
        return ChatActionListBubbleViewHolder.LAYOUT
    }

    override fun type(fallbackAttachmentUiModel: FallbackAttachmentUiModel): Int {
        return ChatbotFallbackAttachmentViewHolder.LAYOUT
    }

    // This case will never run
    override fun type(attachInvoiceSentUiModel: AttachInvoiceSentUiModel): Int {
        return ATTACH_INVOICE_CHAT_COMMON
    }

    override fun type(typingViewModel: TypingChatModel): Int {
        return ChatBotTypingChatViewHolder.LAYOUT
    }

    override fun type(videoUploadUiModel: VideoUploadUiModel): Int {
        return ChatbotVideoUploadViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ChatBotTypingChatViewHolder.LAYOUT -> ChatBotTypingChatViewHolder(parent)
            RightChatMessageUnifyViewHolder.LAYOUT -> RightChatMessageUnifyViewHolder(parent, chatLinkHandlerListener, replyBubbleListener, userSession)
            ChatbotLiveChatSeparatorViewHolder.LAYOUT -> ChatbotLiveChatSeparatorViewHolder(parent)
            AttachedInvoiceSentViewHolder.LAYOUT -> AttachedInvoiceSentViewHolder(parent)
            AttachedInvoiceSelectionViewHolder.LAYOUT -> AttachedInvoiceSelectionViewHolder(parent, attachedInvoiceSelectionListener)
            ChatActionListBubbleViewHolder.LAYOUT -> ChatActionListBubbleViewHolder(parent, chatActionListBubbleListener, chatLinkHandlerListener)
            ChatbotImageUploadViewHolder.LAYOUT -> ChatbotImageUploadViewHolder(parent, imageUploadListener, userSession)
            ChatbotVideoUploadViewHolder.LAYOUT -> ChatbotVideoUploadViewHolder(parent, videoUploadListener)
            DynamicAttachmentTextViewHolder.LAYOUT -> DynamicAttachmentTextViewHolder(
                parent,
                chatLinkHandlerListener,
                replyBubbleListener
            )

            else -> super.createViewHolder(parent, type)
        }
    }

    companion object {
        const val ATTACH_INVOICE_CHAT_COMMON = -1
    }
}

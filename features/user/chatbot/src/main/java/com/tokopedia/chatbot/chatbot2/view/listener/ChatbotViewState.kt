package com.tokopedia.chatbot.chatbot2.view.listener

import android.app.Activity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatbotViewState : BaseChatViewState {
    fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel)

    fun onCheckToHideQuickReply(visitable: Visitable<*>)

    fun onReceiveQuickReplyEvent(visitable: QuickReplyListUiModel)

    fun onReceiveQuickReplyEventWithActionButton(visitable: ChatActionSelectionBubbleUiModel)

    fun onReceiveQuickReplyEventWithChatRating(visitable: ChatRatingUiModel)

    fun onShowInvoiceToChat(generatedInvoice: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel)

    fun removeInvoiceCarousel()

    fun onSuccessSendRating(
        element: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo,
        rating: Int,
        chatRatingUiModel: ChatRatingUiModel,
        activity: Activity
    )

    fun onImageUpload(it: ImageUploadUiModel)

    fun onVideoUpload(it: VideoUploadUiModel)

    fun scrollToBottom()

    fun showLiveChatSeprator(chatSepratorUiModel: ChatSepratorUiModel)

    fun hideEmptyMessage(visitable: Visitable<*>)

    fun showLiveChatQuickReply(quickReplyList: List<QuickReplyUiModel>)

    fun hideActionBubble(model: ChatActionSelectionBubbleUiModel)

    fun hideOptionList(model: HelpFullQuestionsUiModel)

    fun hideCsatOptionList(model: CsatOptionsUiModel)

    fun hideActionBubbleOnSenderMsg()

    fun showRetryUploadImages(it: ImageUploadUiModel, b: Boolean)

    fun removeDummy(visitable: Visitable<*>)

    fun hideInvoiceList()

    fun hideHelpfullOptions()

    fun clearChatOnLoadChatHistory()

    fun clearDuplicate(list: List<Visitable<*>>): ArrayList<Visitable<*>>

    fun handleReplyBox(isEnable: Boolean)

    fun showRetryUploadVideos(it: VideoUploadUiModel)

    fun onSendingMessage(it: MessageUiModel)

    fun onSendingMessage(
        messageId: String,
        userId: String,
        name: String,
        sendMessage: String,
        startTime: String,
        parentReply: ParentReply?
    )
    fun hideDummyVideoAttachment()

    fun hideQuickReplyOnClick()

    fun removeDynamicStickyButton()

    fun handleQuickReplyFromDynamicAttachment(
        toShow: Boolean,
        rejectReasons: DynamicAttachmentRejectReasons
    )

    fun handleQuickReplyFromDynamicAttachment(
        toShow: Boolean,
        quickReplyUiModel: List<QuickReplyUiModel>
    )
}

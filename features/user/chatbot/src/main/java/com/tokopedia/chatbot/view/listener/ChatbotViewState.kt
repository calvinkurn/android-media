package com.tokopedia.chatbot.view.listener

import android.app.Activity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo

/**
 * @author by nisie on 10/12/18.
 */
interface ChatbotViewState : BaseChatViewState {
    fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel)

    fun onCheckToHideQuickReply(visitable: Visitable<*>)

    fun onReceiveQuickReplyEvent(visitable: QuickReplyListUiModel)

    fun onReceiveQuickReplyEventWithActionButton(visitable: ChatActionSelectionBubbleUiModel)

    fun onReceiveQuickReplyEventWithChatRating(visitable: ChatRatingUiModel)

    fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentUiModel)

    fun removeInvoiceCarousel()

    fun onSuccessSendRating(
        element: SendRatingPojo,
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
}

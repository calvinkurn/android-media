package com.tokopedia.chatbot.view.listener

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput

/**
 * @author by nisie on 07/12/18.
 */
interface ChatbotContract {
    interface View : BaseChatContract.View {
        fun onUploadUndersizedImage()

        fun onUploadOversizedImage()

        fun showSnackbarError(stringId: Int)

        fun clearChatText()

        fun openCsat(csatResponse: WebSocketCsatResponse)

        fun onReceiveChatSepratorEvent(chatSepratorUiModel: ChatSepratorUiModel, quickReplyList: List<QuickReplyUiModel>)

        fun showErrorToast(it: Throwable)

        fun updateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?)

        fun removeDummy(visitable: Visitable<*>)

        fun loadChatHistory()

        fun startNewSession()

        fun blockTyping()

        fun enableTyping()

        fun uploadUsingSecureUpload(data: Intent)

        fun sendInvoiceForArticle()

        fun onSuccessGetTickerData(tickerData: TickerData)

        fun onError(throwable: Throwable)

        fun onSuccessSubmitCsatRating(msg: String)

        fun onSuccessSubmitChatCsat(msg: String)

        fun visibilityReplyBubble(state: Boolean)

        fun onSuccessSendRating(pojo: SendRatingPojo, rating: Int, element: ChatRatingUiModel)

        fun sessionChangeStateHandler(state : Boolean)

        fun videoUploadEligibilityHandler(state : Boolean)

        fun onVideoUploadChangeView(uiModel : VideoUploadUiModel)

        fun setBigReplyBoxTitle(text: String, placeholder: String)

        fun hideReplyBox()

        fun showErrorLayout(throwable: Throwable)

    }

    interface Presenter : BaseChatContract.Presenter<View> {

        fun setPageSource(pageSource: String)

        fun sendInvoiceAttachment(messageId: String, invoiceLinkPojo: InvoiceLinkPojo, startTime: String, opponentId: String, isArticleEntry: Boolean, usedBy: String)

        fun sendQuickReply(messageId: String, quickReply: QuickReplyUiModel, startTime: String, opponentId: String)

        fun sendQuickReplyInvoice(messageId: String, quickReply: QuickReplyUiModel, startTime: String, opponentId: String, event: String, usedBy: String)

        fun generateInvoice(invoiceLinkPojo: InvoiceLinkPojo, senderId: String): AttachInvoiceSentUiModel

        fun getExistingChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccess: (ChatroomViewModel, ChatReplies) -> Unit,
            onGetChatRatingListMessageError: (String) -> Unit
        )

        fun connectWebSocket(messageId: String)

        fun sendRating(messageId: String, rating: Int, element: ChatRatingUiModel)

        fun submitCsatRating(messageId: String,
                             inputItem: InputItem)

        fun showTickerData(messageId: String)

        fun sendActionBubble(
            messageId: String,
            selected: ChatActionBubbleUiModel,
            startTime: String,
            opponentId: String
        )

        fun sendReadEvent(messageId: String)

        fun destroyWebSocket()

        fun hitGqlforOptionList(messageId : String, selectedValue: Int, model: HelpFullQuestionsUiModel?)

        fun submitChatCsat(messageId : String,
                           input: ChipSubmitChatCsatInput)

        fun cancelImageUpload()

        fun getActionBubbleforNoTrasaction(): ChatActionBubbleUiModel

        fun checkLinkForRedirection(
            messageId: String,
            invoiceRefNum: String,
            onGetSuccessResponse: (String) -> Unit,
            setStickyButtonStatus: (Boolean) -> Unit,
            onError: (Throwable) -> Unit
        )

        fun checkForSession(messageId: String)
        fun checkUploadSecure(messageId: String, data: Intent)
        fun uploadImageSecureUpload(
            imageUploadViewModel: ImageUploadUiModel,
            messageId: String,
            opponentId: String,
            onErrorImageUpload: (Throwable, ImageUploadUiModel) -> Unit,
            path: String?,
            context: Context?
        )

        fun createAttachInvoiceSingleViewModel(hashMap: Map<String, String>): AttachInvoiceSingleUiModel

        fun getValuesForArticleEntry(uri: Uri): Map<String, String>

        fun sendVideoAttachment(filePath: String, startTime: String, messageId: String)

        fun cancelVideoUpload(file: String, sourceId: String, onError: (Throwable) -> Unit)

        fun checkUploadVideoEligibility(msgId : String)

        fun sendMessage(
            messageId: String,
            sendMessage: String,
            startTime: String,
            opponentId: String,
            parentReply: ParentReply?,
            onSendingMessage: () -> Unit
        )

        fun clearGetChatUseCase()

        fun setBeforeReplyTime(createTime: String)

        fun getTopChat(
            messageId: String,
            onSuccess: (ChatroomViewModel, ChatReplies) -> Unit,
            onError: (Throwable) -> Unit,
            onGetChatRatingListMessageError: (String) -> Unit
        )

        fun getBottomChat(
            messageId: String,
            onSuccess: (ChatroomViewModel, ChatReplies) -> Unit,
            onError: (Throwable) -> Unit,
            onGetChatRatingListMessageError: (String) -> Unit
        )
    }
}

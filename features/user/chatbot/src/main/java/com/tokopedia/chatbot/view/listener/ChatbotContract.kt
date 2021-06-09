package com.tokopedia.chatbot.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
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

        fun onReceiveConnectionEvent(connectionDividerViewModel: ConnectionDividerViewModel, quickReplyList: List<QuickReplyViewModel>)

        fun onReceiveChatSepratorEvent(chatSepratorViewModel: ChatSepratorViewModel, quickReplyList: List<QuickReplyViewModel>)

        fun isBackAllowed(isBackAllowed: Boolean)

        fun onClickLeaveQueue()

        fun showErrorToast(it: Throwable)

        fun updateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?)

        fun removeDummy(visitable: Visitable<*>)
    }

    interface Presenter : BaseChatContract.Presenter<View> {

        fun sendInvoiceAttachment(messageId: String, invoiceLinkPojo: InvoiceLinkPojo, startTime: String, opponentId: String)

        fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel, startTime: String, opponentId: String)

        fun generateInvoice(invoiceLinkPojo: InvoiceLinkPojo, senderId: String)
                : AttachInvoiceSentViewModel

        fun getExistingChat(messageId: String,
                            onError: (Throwable) -> Unit,
                            onSuccess: (ChatroomViewModel) -> Unit,
                            onGetChatRatingListMessageError: (String) -> Unit)

        fun loadPrevious(messageId: String,
                         page: Int,
                         onError: (Throwable) -> Unit,
                         onSuccess: (ChatroomViewModel) -> Unit,
                         onGetChatRatingListMessageError: (String) -> Unit)


        fun connectWebSocket(messageId: String)

        fun sendRating(messageId : String, rating: Int, timestamp : String,
                       onError: (Throwable) -> Unit,
                       onSuccess: (SendRatingPojo) -> Unit)

        fun sendReasonRating(messageId: String, reason: String, timestamp: String,
                             onError: (Throwable) -> Unit,
                             onSuccess: (String) -> Unit)
        fun submitCsatRating(inputItem: InputItem,
                             onError: (Throwable) -> Unit,
                             onSuccess: (String) -> Unit)

        fun showTickerData(onError: (Throwable) -> Unit,
                           onSuccesGetTickerData: (TickerData) -> Unit)

        fun sendActionBubble(messageId: String, selected: ChatActionBubbleViewModel,
                             startTime: String,
                             opponentId: String)

        fun sendReadEvent(messageId: String)

        fun uploadImages(it: ImageUploadViewModel,
                         messageId : String,
                         opponentId : String,
                         onError: (Throwable, ImageUploadViewModel) -> Unit)

        fun destroyWebSocket()

        fun hitGqlforOptionList(selectedValue: Int, model: HelpFullQuestionsViewModel?)

        fun submitChatCsat(input: ChipSubmitChatCsatInput,
                           onsubmitingChatCsatSuccess: (String) -> Unit,
                           onError: (Throwable) -> Unit)

        fun cancelImageUpload()

        fun getActionBubbleforNoTrasaction(): ChatActionBubbleViewModel

        fun checkLinkForRedirection(invoiceRefNum: String,
                                    onGetSuccessResponse: (String) -> Unit,
                                    setStickyButtonStatus: (Boolean) -> Unit,
                                    onError: (Throwable) -> Unit)

    }
}
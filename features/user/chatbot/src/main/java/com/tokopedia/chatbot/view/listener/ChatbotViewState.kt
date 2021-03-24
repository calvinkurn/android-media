package com.tokopedia.chatbot.view.listener

import android.app.Activity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo

/**
 * @author by nisie on 10/12/18.
 */
interface ChatbotViewState : BaseChatViewState {
    fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel)

    fun onCheckToHideQuickReply(visitable: Visitable<*>)

    fun onReceiveQuickReplyEvent(visitable: QuickReplyListViewModel)

    fun onReceiveQuickReplyEventWithActionButton(visitable: ChatActionSelectionBubbleViewModel)

    fun onReceiveQuickReplyEventWithChatRating(visitable: ChatRatingViewModel)

    fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel)

    fun onSuccessSendRating(element: SendRatingPojo, rating: Int,
                            chatRatingViewModel : ChatRatingViewModel,
                            activity: Activity,
                            onClickReasonRating: (String) -> Unit)

    fun onClickReasonRating()

    fun onImageUpload(it: ImageUploadViewModel)

    fun scrollToBottom()

    fun showDividerViewOnConnection(connectionDividerViewModel: ConnectionDividerViewModel)

    fun showLiveChatSeprator(chatSepratorViewModel: ChatSepratorViewModel)

    fun hideEmptyMessage(visitable: Visitable<*>)

    fun showLiveChatQuickReply(quickReplyList: List<QuickReplyViewModel>)

    fun hideActionBubble(model: ChatActionSelectionBubbleViewModel)

    fun hideOptionList(model: HelpFullQuestionsViewModel)

    fun hideCsatOptionList(model: CsatOptionsViewModel)

    fun hideActionBubbleOnSenderMsg()

    fun showRetryUploadImages(it: ImageUploadViewModel, b: Boolean)

    fun removeDummy(visitable: Visitable<*>)

    fun hideInvoiceList()

}
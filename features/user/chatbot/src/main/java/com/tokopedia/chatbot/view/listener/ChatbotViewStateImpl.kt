package com.tokopedia.chatbot.view.listener

import android.app.Activity
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactoryImpl
import com.tokopedia.chatbot.view.adapter.QuickReplyAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.AttachedInvoiceSelectionListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatActionListBubbleListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatRatingListener
import com.tokopedia.chatbot.view.customview.ReasonBottomSheet
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

/**
 * @author by nisie on 07/12/18.
 */
class ChatbotViewStateImpl(@NonNull override val view: View,
                           @NonNull private val userSession: UserSessionInterface,
                           private val imageAnnouncementListener: ImageAnnouncementListener,
                           private val chatLinkHandlerListener: ChatLinkHandlerListener,
                           private val imageUploadListener: ImageUploadListener,
                           private val productAttachmentListener: ProductAttachmentListener,
                           private val attachedInvoiceSelectionListener: AttachedInvoiceSelectionListener,
                           private val chatRatingListener: ChatRatingListener,
                           private val chatActionListBubbleListener: ChatActionListBubbleListener,
                           override val toolbar: Toolbar
) : BaseChatViewStateImpl(view, toolbar), ChatbotViewState {


    private lateinit var adapter: ChatbotAdapter
    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var rvQuickReply: RecyclerView
    private lateinit var reasonBottomSheet: ReasonBottomSheet

    override fun initView() {
        super.initView()

        adapter = ChatbotAdapter(ChatbotTypeFactoryImpl(imageAnnouncementListener,
                chatLinkHandlerListener, imageUploadListener, productAttachmentListener,
                attachedInvoiceSelectionListener, chatRatingListener, chatActionListBubbleListener)
                , ArrayList())

        recyclerView.adapter = adapter

    }

    override fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel) {
        hideLoading()
        adapter.addElement(chatroomViewModel.listChat)
        scrollToBottom()
        updateHeader(chatroomViewModel)
        showReplyBox()
        showActionButtons()
        checkShowQuickReply(chatroomViewModel)
    }

    private fun checkShowQuickReply(chatroomViewModel: ChatroomViewModel) {
        if (chatroomViewModel.listChat.isNotEmpty()
                && chatroomViewModel.listChat[0] is QuickReplyListViewModel) {
            showQuickReply(chatroomViewModel.listChat[0] as QuickReplyListViewModel)
        }
    }

    override fun onCheckToHideQuickReply(visitable: Visitable<*>) {
        if (visitable is BaseChatViewModel
                && TextUtils.isEmpty(visitable.attachmentId)
                && hasQuickReply()
                && !isMyMessage(visitable.fromUid)) {
            hideQuickReply()
        }
    }

    override fun onReceiveQuickReplyEvent(visitable: QuickReplyListViewModel) {
        showQuickReply(visitable)
    }

    override fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel) {
        adapter.addElement(generatedInvoice)
        scrollToBottom()
    }

    override fun onSendRating(element: ChatRatingViewModel, rating: Int) {
        //TODO DISABLE RATING BUTTON
    }

    override fun onSuccessSendRating(element: ChatRatingViewModel, rating: Int, activity: Activity,
                                     onClickReasonRating: Unit) {
        if (rating == ChatRatingViewModel.RATING_BAD) {
            showReasonBottomSheet(element, activity, onClickReasonRating)
        }
    }

    private fun showReasonBottomSheet(element: ChatRatingViewModel, activity: Activity, onReasonClicked: Unit) {
        //TODO
//        if(!::reasonBottomSheet.isInitialized){
//            reasonBottomSheet = ReasonBottomSheet.createInstance(activity, )
//        }
//        reasonBottomSheet.show()
    }

    private fun isMyMessage(fromUid: String?): Boolean {
        return fromUid != null && userSession.userId == fromUid
    }

    private fun showQuickReply(quickReplyListViewModel: QuickReplyListViewModel) {
        //TODO SHOW QUICK REPLY
    }

    private fun hasQuickReply(): Boolean {
        return quickReplyAdapter != null && rvQuickReply != null
    }

    private fun hideQuickReply() {
        quickReplyAdapter.clearData()
        rvQuickReply.visibility = View.GONE
    }

    private fun showActionButtons() {
        pickerButton.visibility = View.VISIBLE
        attachProductButton.visibility = View.GONE
        maximizeButton.visibility = View.GONE
    }

    override fun onSendingMessage(messageId: String, userId: String, name: String, sendMessage:
    String) {
        adapter.addElement(
                MessageViewModel(
                        messageId,
                        userId,
                        name,
                        SendableViewModel.generateStartTime(),
                        sendMessage
                )
        )
    }

}
package com.tokopedia.chatbot.view.listener

import android.app.Activity
import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.view.adapter.QuickReplyAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.customview.ReasonBottomSheet
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 07/12/18.
 */
class ChatbotViewStateImpl(@NonNull override val view: View,
                           @NonNull private val userSession: UserSessionInterface,
                           private val quickReplyListener: QuickReplyListener,
                           private val typingListener: TypingListener,
                           private val onAttachImageClicked: () -> Unit,
                           override val toolbar: Toolbar,
                           private val adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>
) : BaseChatViewStateImpl(view, toolbar, typingListener), ChatbotViewState {

    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var rvQuickReply: RecyclerView
    private lateinit var reasonBottomSheet: ReasonBottomSheet

    override fun initView() {
        super.initView()

        rvQuickReply = view.findViewById(R.id.list_quick_reply)
        quickReplyAdapter = QuickReplyAdapter(QuickReplyListViewModel(), quickReplyListener)

        rvQuickReply.layoutManager = LinearLayoutManager(rvQuickReply.context,
                LinearLayoutManager.HORIZONTAL, false)
        rvQuickReply.adapter = quickReplyAdapter

        pickerButton.setOnClickListener {
            onAttachImageClicked()
        }

    }

    override fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel) {
        scrollToBottom()
        updateHeader(chatroomViewModel) {}
        showReplyBox(chatroomViewModel.replyable)
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

    override fun checkLastCompletelyVisibleItemIsFirst(): Boolean {
        //always scroll to bottom
        return true
    }

    override fun onReceiveQuickReplyEvent(visitable: QuickReplyListViewModel) {
        super.onReceiveMessageEvent(visitable)
        showQuickReply(visitable)
    }

    override fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel) {
        super.onReceiveMessageEvent(generatedInvoice)
    }

    override fun onSuccessSendRating(element: SendRatingPojo, rating: Int,
                                     chatRatingViewModel: ChatRatingViewModel,
                                     activity: Activity,
                                     onClickReasonRating: (String) -> Unit) {
        val indexToUpdate = adapter.data.indexOf(chatRatingViewModel)
        if (adapter.data[indexToUpdate] is ChatRatingViewModel) {
            (adapter.data[indexToUpdate] as ChatRatingViewModel).ratingStatus = rating
            adapter.notifyItemChanged(indexToUpdate)
        }

        if (rating == ChatRatingViewModel.RATING_BAD) {
            showReasonBottomSheet(element, activity, onClickReasonRating)
        }
    }

    private fun showReasonBottomSheet(element: SendRatingPojo, activity: Activity,
                                      onClickReasonRating: (String) -> Unit) {
        if (!::reasonBottomSheet.isInitialized) {
            reasonBottomSheet = ReasonBottomSheet.createInstance(activity,
                    element.postRatingV2.data.listReason, onClickReasonRating)
        }
        reasonBottomSheet.show()
    }

    override fun onClickReasonRating() {
        if (::reasonBottomSheet.isInitialized) {
            reasonBottomSheet.dismiss()
        }
    }

    override fun onImageUpload(it: ImageUploadViewModel) {
        getAdapter().addElement(it)
    }

    private fun isMyMessage(fromUid: String?): Boolean {
        return fromUid != null && userSession.userId == fromUid
    }

    private fun showQuickReply(quickReplyListViewModel: QuickReplyListViewModel) {
        if (::quickReplyAdapter.isInitialized) {
            quickReplyAdapter.setList(quickReplyListViewModel)
            quickReplyAdapter.notifyDataSetChanged()
        }
        rvQuickReply.visibility = View.VISIBLE
    }

    private fun hasQuickReply(): Boolean {
        return ::quickReplyAdapter.isInitialized
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

}
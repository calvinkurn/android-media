package com.tokopedia.chatbot.view.listener

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.QuickReplyAdapter
import com.tokopedia.chatbot.view.customview.ReasonBottomSheet
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 07/12/18.
 */
class ChatbotViewStateImpl(@NonNull override val view: View,
                           @NonNull private val userSession: UserSessionInterface
) : BaseChatViewStateImpl(view), ChatbotViewState {

    private lateinit var pickerButton: View

    private lateinit var adapter: ChatbotAdapter
    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var rvQuickReply: RecyclerView
    private lateinit var reasonBottomSheet: ReasonBottomSheet

    override fun initView() {
        pickerButton = view.findViewById(R.id.image_picker)
    }

    override fun onSuccessLoadFirstTime(list: ArrayList<Visitable<*>>) {
        hideLoading()
        adapter.addElement(list)
        scrollToBottom()
        showReplyBox()
        showPickerButton()
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
        showQuickReply()
    }

    override fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel) {
        adapter.addElement(generatedInvoice)
        scrollToBottom()
    }

    private fun isMyMessage(fromUid: String?): Boolean {
        return fromUid != null && userSession.userId == fromUid
    }

    private fun showQuickReply() {

    }

    private fun hasQuickReply(): Boolean {
        return quickReplyAdapter != null && rvQuickReply != null
    }

    private fun hideQuickReply() {
        quickReplyAdapter.clearData()
        rvQuickReply.visibility = View.GONE
    }

    private fun showPickerButton() {
        pickerButton.visibility = View.VISIBLE
    }

}
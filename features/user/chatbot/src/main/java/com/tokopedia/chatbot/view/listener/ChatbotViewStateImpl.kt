package com.tokopedia.chatbot.view.listener

import android.app.Activity
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.SHOW_TEXT
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
                           typingListener: TypingListener,
                           attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
                           override val toolbar: Toolbar,
                           private val adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>
) : BaseChatViewStateImpl(view, toolbar, typingListener, attachmentMenuListener), ChatbotViewState {

    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var rvQuickReply: RecyclerView
    private lateinit var reasonBottomSheet: ReasonBottomSheet
    private lateinit var chatMenuBtn: ImageView

    override fun initView() {
        recyclerView = view.findViewById(getRecyclerViewId())
        mainLoading = view.findViewById(getProgressId())
        replyEditText = view.findViewById(getNewCommentId())
        replyBox = view.findViewById(getReplyBoxId())
        actionBox = view.findViewById(getActionBoxId())
        sendButton = view.findViewById(getSendButtonId())
        notifier = view.findViewById(getNotifierId())
        chatMenuButton = view.findViewById(getChatMenuId())

        chatMenuBtn = view.findViewById(R.id.iv_chat_menu)
        rvQuickReply = view.findViewById(R.id.list_quick_reply)
        quickReplyAdapter = QuickReplyAdapter(QuickReplyListViewModel(), quickReplyListener)

        rvQuickReply.layoutManager = LinearLayoutManager(rvQuickReply.context,
                LinearLayoutManager.HORIZONTAL, false)
        rvQuickReply.adapter = quickReplyAdapter

        super.initView()
    }


    override fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel) {
        scrollToBottom()
        updateHeader(chatroomViewModel) {}
        showReplyBox(chatroomViewModel.replyable)
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

    /**
     * IN LIST OF CHAT MESSAGES,
     * IF
     * FIRST ELEMENT IS TYPE OF  ConnectionDividerViewModel
     * I AM REPLACING THE ELEMENT
     * ELSE
     * ADDING A NEW ELEMENT
     */

    override fun showDividerViewOnConnection(connectionDividerViewModel: ConnectionDividerViewModel) {
        if (connectionDividerViewModel.type.equals(SHOW_TEXT,true)) {
            if (getAdapter().list[0] is ConnectionDividerViewModel) {
                getAdapter().setElement(0, connectionDividerViewModel)
            } else {
                getAdapter().addElement(0, connectionDividerViewModel)
            }
            getAdapter().removeTyping()
        } else {
            getAdapter().removeElement(connectionDividerViewModel)
        }
    }

    override fun getRecyclerViewId(): Int {
        return R.id.recycler_view
    }

    override fun getProgressId(): Int {
        return R.id.progress
    }

    override fun getNewCommentId(): Int {
        return R.id.new_comment
    }

    override fun getReplyBoxId(): Int {
        return R.id.reply_box
    }

    override fun getActionBoxId(): Int {
        return R.id.add_comment_area
    }

    override fun getSendButtonId(): Int {
        return R.id.send_but
    }

    override fun getNotifierId(): Int {
        return R.id.notifier
    }

    override fun getChatMenuId(): Int {
        return R.id.iv_chat_menu
    }

    override fun getAttachmentMenuId() = R.id.rv_attachment_menu
    override fun getRootViewId() = R.id.main
    override fun getAttachmentMenuContainer(): Int = R.id.rv_attachment_menu_container
}
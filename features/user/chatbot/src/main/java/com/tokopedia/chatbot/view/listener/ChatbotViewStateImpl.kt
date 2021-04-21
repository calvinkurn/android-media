package com.tokopedia.chatbot.view.listener

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics.Companion.chatbotAnalytics
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.SHOW_TEXT
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.QuickReplyAdapter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.view.customview.ReasonBottomSheet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 07/12/18.
 */
private const val ACTION_IMRESSION_ACTION_BUTTON = "impression action button"
private const val ACTION_IMRESSION_THUMBS_UP_THUMBS_DOWN = "impression thumbs up and thumbs down"

class ChatbotViewStateImpl(@NonNull override val view: View,
                           @NonNull private val userSession: UserSessionInterface,
                           private val quickReplyListener: QuickReplyListener,
                           typingListener: TypingListener,
                           attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
                           override val toolbar: Toolbar,
                           private val adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>,
                           private val onChatMenuButtonClicked: () -> Unit
) : BaseChatViewStateImpl(view, toolbar, typingListener, attachmentMenuListener), ChatbotViewState {

    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var rvQuickReply: RecyclerView
    private lateinit var reasonBottomSheet: ReasonBottomSheet
    private lateinit var chatMenuBtn: ImageView

    override fun initView() {
        recyclerView = view.findViewById(getRecyclerViewId())
        replyEditText = view.findViewById(getNewCommentId())
        replyBox = view.findViewById(getReplyBoxId())
        actionBox = view.findViewById(getActionBoxId())
        sendButton = view.findViewById(getSendButtonId())
        notifier = view.findViewById(getNotifierId())
        chatMenuButton = view.findViewById(getChatMenuId())

        chatMenuBtn = view.findViewById(R.id.iv_chat_menu)
        rvQuickReply = view.findViewById(R.id.list_quick_reply)
        quickReplyAdapter = QuickReplyAdapter(getQuickReplyList(), quickReplyListener)

        rvQuickReply.layoutManager = LinearLayoutManager(rvQuickReply.context,
                LinearLayoutManager.HORIZONTAL, false)
        rvQuickReply.adapter = quickReplyAdapter

        super.initView()
    }

    private fun getQuickReplyList(): List<QuickReplyViewModel> {
        return if (QuickReplyListViewModel().quickReplies.isNullOrEmpty()) {
            if (ChatActionSelectionBubbleViewModel().quickReplies.isNullOrEmpty()) {
                ChatRatingViewModel().quickReplies
            } else {
                ChatActionSelectionBubbleViewModel().quickReplies
            }
        } else {
            QuickReplyListViewModel().quickReplies
        }
    }


    override fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel) {
        scrollToBottom()
        updateHeader(chatroomViewModel) {}
        showReplyBox(chatroomViewModel.replyable)
        checkShowQuickReply(chatroomViewModel)
    }

    override fun loadAvatar(avatarUrl: String) {
        val avatar = toolbar.findViewById<ImageView>(R.id.user_avatar)
        ImageHandler.loadImageCircle2(avatar.context, avatar, avatarUrl,
                R.drawable.chatbot_avatar)
    }

    private fun checkShowQuickReply(chatroomViewModel: ChatroomViewModel) {
        if (chatroomViewModel.listChat.isNotEmpty()
                && chatroomViewModel.listChat[0] is QuickReplyListViewModel) {
            showQuickReply((chatroomViewModel.listChat[0] as QuickReplyListViewModel).quickReplies)
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
        showQuickReply(visitable.quickReplies)
    }

    override fun onReceiveQuickReplyEventWithActionButton(visitable: ChatActionSelectionBubbleViewModel) {
        super.onReceiveMessageEvent(visitable)
        chatbotAnalytics.eventShowView(ACTION_IMRESSION_ACTION_BUTTON)
        showQuickReply(visitable.quickReplies)
    }

    override fun onReceiveQuickReplyEventWithChatRating(visitable: ChatRatingViewModel) {
        super.onReceiveMessageEvent(visitable)
        chatbotAnalytics.eventShowView(ACTION_IMRESSION_THUMBS_UP_THUMBS_DOWN)
        showQuickReply(visitable.quickReplies)
    }

    override fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel) {
        removeInvoiceCarousel()
        super.onReceiveMessageEvent(generatedInvoice)
    }

    private fun removeInvoiceCarousel() {
        var item: AttachInvoiceSelectionViewModel? = null
        for (it in adapter.list) {
            if (it is AttachInvoiceSelectionViewModel) {
                item = it
                break
            }
        }

        if (item != null && adapter.list.isNotEmpty()) {
            adapter.clearElement(item)
        }
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
        scrollDownWhenInBottom()
    }

    private fun isMyMessage(fromUid: String?): Boolean {
        return fromUid != null && userSession.userId == fromUid
    }

    private fun showQuickReply(list: List<QuickReplyViewModel>) {
            if (::quickReplyAdapter.isInitialized && list.isNotEmpty()) {
                quickReplyAdapter.setList(list)
                rvQuickReply.visibility = View.VISIBLE
            }
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


    override fun showLiveChatSeprator(chatSepratorViewModel: ChatSepratorViewModel) {
        getAdapter().addElement(0, chatSepratorViewModel)
    }

    override fun hideEmptyMessage(visitable: Visitable<*>) {
        if (visitable is FallbackAttachmentViewModel && visitable.message.isEmpty()) {
            getAdapter().removeElement(visitable)
        }
    }


    override fun showLiveChatQuickReply(quickReplyList: List<QuickReplyViewModel>) {
        showQuickReply(quickReplyList)
    }

    override fun hideActionBubble(model: ChatActionSelectionBubbleViewModel) {
        val adapter = getAdapter()
        if (adapter.list.isNotEmpty() && adapter.list[0] is ChatActionSelectionBubbleViewModel ){
            adapter.removeElement(model)
        }
    }

    override fun hideOptionList(model: HelpFullQuestionsViewModel) {
        val adapter = getAdapter()
        if (adapter.list.isNotEmpty() && adapter.list[0] is HelpFullQuestionsViewModel) {
            model.isSubmited = true
            adapter.setElement(0, model)
        }
    }

    override fun hideCsatOptionList(model: CsatOptionsViewModel) {
        val adapter = getAdapter()
        var position: Int = 0
        for (msg in adapter.list) {
            if (msg is CsatOptionsViewModel && model.csat?.caseChatId == msg.csat?.caseChatId) {
                model.isSubmited = true
                adapter.setElement(position, model)
                break;
            }
            position++
        }
    }

    override fun hideActionBubbleOnSenderMsg() {
        var item: ChatActionSelectionBubbleViewModel? = null
        for (it in adapter.list) {
            if (it is ChatActionSelectionBubbleViewModel) {
                item = it
                break
            }
        }

        if (item != null && adapter.list.isNotEmpty()) {
            adapter.clearElement(item)
        }
    }

    override fun showRetryUploadImages(image: ImageUploadViewModel, retry: Boolean){
        getAdapter().showRetryFor(image, retry)
    }

    override fun removeDummy(visitable: Visitable<*>) {
        getAdapter().removeDummy(visitable)
    }

    override fun hideInvoiceList() {
        removeInvoiceCarousel()
    }

    override fun hideHelpfullOptions() {
        var item: HelpFullQuestionsViewModel? = null
        for (it in adapter.list) {
            if (it is HelpFullQuestionsViewModel) {
                item = it
                break
            }
        }
        item?.let { hideOptionList(it) }
    }

    override fun getAdapter(): ChatbotAdapter {
        return super.getAdapter() as ChatbotAdapter
    }

    override fun updateHeader(chatroomViewModel: ChatroomViewModel, onToolbarClicked: () -> Unit) {
        val title = toolbar.findViewById<TextView>(R.id.title)
        val interlocutorName = getInterlocutorName(chatroomViewModel.getHeaderName())
        title.text = MethodChecker.fromHtml(interlocutorName)
        loadAvatar(chatroomViewModel.headerModel.image)
    }

    override fun getInterlocutorName(headerName: String): String  = headerName

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        val title = notifier.findViewById<TextView>(R.id.title)
        val action = notifier.findViewById<View>(R.id.action)
        if (isWebSocketError) {
            notifier.show()
            title.setText(com.tokopedia.chat_common.R.string.error_no_connection_retrying);
            action.show()

        } else {
            action.hide()
            notifier.hide()
        }
    }

    override fun setupChatMenu() {
        chatMenuButton.setOnClickListener {
            onChatMenuButtonClicked.invoke()
        }
    }

    override fun getRecyclerViewId(): Int {
        return R.id.recycler_view
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
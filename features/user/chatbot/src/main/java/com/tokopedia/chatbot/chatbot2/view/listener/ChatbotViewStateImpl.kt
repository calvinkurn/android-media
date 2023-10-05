package com.tokopedia.chatbot.chatbot2.view.listener

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
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.chatbot2.view.adapter.QuickReplyAdapter
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.QuickReplyListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.chatbot2.view.util.helper.toQuickReplyUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 07/12/18.
 */
private const val ACTION_IMRESSION_ACTION_BUTTON = "impression action button"
private const val ACTION_IMRESSION_THUMBS_UP_THUMBS_DOWN = "impression thumbs up and thumbs down"

@Suppress("LateinitUsage")
class ChatbotViewStateImpl(
    @NonNull override val view: View,
    @NonNull private val userSession: UserSessionInterface,
    private val quickReplyListener: QuickReplyListener,
    typingListener: TypingListener,
    attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
    override val toolbar: Toolbar,
    private val adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>,
    val sendAnalytics: (impressionType: String) -> Unit
) : BaseChatViewStateImpl(view, toolbar, typingListener, attachmentMenuListener), ChatbotViewState {

    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private var rvQuickReply: RecyclerView? = null
    private var chatMenuBtn: ImageView? = null

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
        quickReplyAdapter = QuickReplyAdapter(getQuickReplyList(), quickReplyListener, sendAnalyticsFromAdapter = { impressionType ->
            sendAnalytics(impressionType)
        })

        rvQuickReply?.layoutManager = LinearLayoutManager(
            rvQuickReply?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvQuickReply?.adapter = quickReplyAdapter

        super.initView()
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
    }

    private fun getQuickReplyList(): List<QuickReplyUiModel> {
        return if (QuickReplyListUiModel().quickReplies.isNullOrEmpty()) {
            if (ChatActionSelectionBubbleUiModel().quickReplies.isNullOrEmpty()) {
                ChatRatingUiModel().quickReplies
            } else {
                ChatActionSelectionBubbleUiModel().quickReplies
            }
        } else {
            QuickReplyListUiModel().quickReplies
        }
    }

    override fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel) {
        scrollToBottom()
        updateHeader(chatroomViewModel) {}
        checkShowQuickReply(chatroomViewModel)
    }

    override fun handleReplyBox(isEnable: Boolean) {
        showReplyBox(isEnable)
    }

    override fun onSendingMessage(it: MessageUiModel) {
        getAdapter().addElement(it)
        scrollDownWhenInBottom()
    }

    override fun onSendingMessage(
        messageId: String,
        userId: String,
        name: String,
        sendMessage: String,
        startTime: String,
        parentReply: ParentReply?
    ) {
        val localId = IdentifierUtil.generateLocalId()
        val message = MessageUiModel.Builder()
            .withMsgId(messageId)
            .withFromUid(userId)
            .withFrom(name)
            .withReplyTime(BaseChatUiModel.SENDING_TEXT)
            .withStartTime(startTime)
            .withMsg(sendMessage)
            .withLocalId(localId)
            .withIsDummy(true)
            .withIsSender(true)
            .withIsRead(false)
            .withParentReply(parentReply)
            .build()
        getAdapter().addElement(message)
    }

    override fun onSendingMessage(
        messageId: String,
        userId: String,
        name: String,
        sendMessage: String,
        startTime: String
    ) {
    }

    override fun hideQuickReplyOnClick() {
        hideQuickReply()
    }

    override fun loadAvatar(avatarUrl: String) {
        val avatar = toolbar.findViewById<ImageView>(R.id.user_avatar)
        ImageHandler.loadImageCircle2(
            avatar.context,
            avatar,
            avatarUrl,
            R.drawable.chatbot_avatar
        )
    }

    override fun clearChatOnLoadChatHistory() {
        adapter.data.clear()
    }

    override fun clearDuplicate(list: List<Visitable<*>>): ArrayList<Visitable<*>> {
        val filteredList = ArrayList<Visitable<*>>(list)
        list.forEach { api ->
            adapter.data.forEach { ws ->
                when {
                    ws is MessageUiModel && api is MessageUiModel -> {
                        if ((ws.replyTime == api.replyTime) && (ws.message == api.message)) {
                            filteredList.remove(api)
                        }
                    }
                    ws is BaseChatUiModel && api is BaseChatUiModel -> {
                        if ((ws.replyTime == api.replyTime) && (ws.message == api.message)) {
                            filteredList.remove(api)
                        }
                    }
                }
            }
        }
        return filteredList
    }

    private fun checkShowQuickReply(chatroomViewModel: ChatroomViewModel) {
        if (chatroomViewModel.listChat.isNotEmpty() &&
            chatroomViewModel.listChat[0] is QuickReplyListUiModel
        ) {
            showQuickReply((chatroomViewModel.listChat[0] as QuickReplyListUiModel).quickReplies)
        }
    }

    override fun onCheckToHideQuickReply(visitable: Visitable<*>) {
        if (visitable is BaseChatUiModel &&
            TextUtils.isEmpty(visitable.attachmentId) &&
            hasQuickReply() &&
            !isMyMessage(visitable.fromUid)
        ) {
            hideQuickReply()
        }
    }

    override fun checkLastCompletelyVisibleItemIsFirst(): Boolean {
        // always scroll to bottom
        return true
    }

    override fun onReceiveQuickReplyEvent(visitable: QuickReplyListUiModel) {
        super.onReceiveMessageEvent(visitable)
        showQuickReply(visitable.quickReplies)
    }

    override fun onReceiveQuickReplyEventWithActionButton(visitable: ChatActionSelectionBubbleUiModel) {
        super.onReceiveMessageEvent(visitable)
        sendAnalytics(ACTION_IMRESSION_ACTION_BUTTON)
        showQuickReply(visitable.quickReplies)
    }

    override fun onReceiveQuickReplyEventWithChatRating(visitable: ChatRatingUiModel) {
        super.onReceiveMessageEvent(visitable)
        sendAnalytics(ACTION_IMRESSION_THUMBS_UP_THUMBS_DOWN)
        showQuickReply(visitable.quickReplies)
    }

    override fun onShowInvoiceToChat(generatedInvoice: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel) {
        removeInvoiceCarousel()
        super.onReceiveMessageEvent(generatedInvoice)
    }

    override fun removeInvoiceCarousel() {
        var item: AttachInvoiceSelectionUiModel? = null
        for (it in adapter.list) {
            if (it is AttachInvoiceSelectionUiModel) {
                item = it
                break
            }
        }

        if (item != null && adapter.list.isNotEmpty()) {
            adapter.clearElement(item)
        }
    }

    override fun onSuccessSendRating(
        element: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo,
        rating: Int,
        chatRatingUiModel: ChatRatingUiModel,
        activity: Activity
    ) {
        val indexToUpdate = adapter.data.indexOf(chatRatingUiModel)
        if (adapter.data[indexToUpdate] is ChatRatingUiModel) {
            (adapter.data[indexToUpdate] as ChatRatingUiModel).ratingStatus = rating
            adapter.notifyItemChanged(indexToUpdate)
        }
    }

    override fun onImageUpload(it: ImageUploadUiModel) {
        getAdapter().addElement(it)
        scrollDownWhenInBottom()
    }

    override fun onVideoUpload(it: VideoUploadUiModel) {
        getAdapter().addElement(it)
        scrollDownWhenInBottom()
    }

    private fun isMyMessage(fromUid: String?): Boolean {
        return fromUid != null && userSession.userId == fromUid
    }

    private fun showQuickReply(list: List<QuickReplyUiModel>, isFromDynamicAttachment: Boolean = false) {
        if (::quickReplyAdapter.isInitialized && list.isNotEmpty()) {
            quickReplyAdapter.setList(list, isFromDynamicAttachment)
            rvQuickReply?.visibility = View.VISIBLE
        }
    }

    private fun hasQuickReply(): Boolean {
        return ::quickReplyAdapter.isInitialized
    }

    private fun hideQuickReply() {
        quickReplyAdapter?.clearData()
        rvQuickReply?.visibility = View.GONE
    }

    /**
     * IN LIST OF CHAT MESSAGES,
     * IF
     * FIRST ELEMENT IS TYPE OF  ConnectionDividerViewModel
     * I AM REPLACING THE ELEMENT
     * ELSE
     * ADDING A NEW ELEMENT
     */

    override fun showLiveChatSeprator(chatSepratorUiModel: ChatSepratorUiModel) {
        getAdapter().addElement(0, chatSepratorUiModel)
    }

    override fun hideEmptyMessage(visitable: Visitable<*>) {
        if (visitable is FallbackAttachmentUiModel && visitable.message.isEmpty()) {
            getAdapter().removeElement(visitable)
        }
    }

    override fun showLiveChatQuickReply(quickReplyList: List<QuickReplyUiModel>) {
        showQuickReply(quickReplyList)
    }

    override fun hideActionBubble(model: ChatActionSelectionBubbleUiModel) {
        val adapter = getAdapter()
        if (adapter.list.isNotEmpty() && adapter.list[0] is ChatActionSelectionBubbleUiModel) {
            adapter.removeElement(model)
        }
    }

    override fun hideOptionList(model: HelpFullQuestionsUiModel) {
        val adapter = getAdapter()
        if (adapter.list.isNotEmpty() && adapter.list[0] is HelpFullQuestionsUiModel) {
            model.isSubmited = true
            adapter.setElement(0, model)
        }
    }

    override fun hideCsatOptionList(model: CsatOptionsUiModel) {
        val adapter = getAdapter()
        var position: Int = 0
        for (msg in adapter.list) {
            if (msg is CsatOptionsUiModel && model.csat?.caseChatId == msg.csat?.caseChatId) {
                model.isSubmited = true
                adapter.setElement(position, model)
                break
            }
            position++
        }
    }

    override fun hideActionBubbleOnSenderMsg() {
        var item: ChatActionSelectionBubbleUiModel? = null
        for (it in adapter.list) {
            if (it is ChatActionSelectionBubbleUiModel) {
                item = it
                break
            }
        }

        if (item != null && adapter.list.isNotEmpty()) {
            adapter.clearElement(item)
        }
    }

    override fun showRetryUploadImages(image: ImageUploadUiModel, retry: Boolean) {
        getAdapter().showRetryFor(image, retry)
    }

    override fun showRetryUploadVideos(video: VideoUploadUiModel) {
        getAdapter().showRetryForVideo(video)
    }

    override fun hideDummyVideoAttachment() {
        getAdapter().removeDummyVideo()
    }

    override fun removeDummy(visitable: Visitable<*>) {
        getAdapter().removeDummy(visitable)
    }

    override fun hideInvoiceList() {
        removeInvoiceCarousel()
    }

    override fun hideHelpfullOptions() {
        var item: HelpFullQuestionsUiModel? = null
        var index = 0
        for (it in adapter.list) {
            if (it is HelpFullQuestionsUiModel) {
                item = it
                index = adapter.list.indexOf(item)
                break
            }
        }
        item?.let {
            it.isSubmited = true
            adapter.setElement(index, it)
        }
    }

    override fun getAdapter(): ChatbotAdapter {
        return super.getAdapter() as ChatbotAdapter
    }

    override fun updateHeader(chatroomViewModel: ChatroomViewModel, onToolbarClicked: () -> Unit) {
        val title = toolbar.findViewById<TextView>(R.id.title)
        val interlocutorName = getInterlocutorName(chatroomViewModel.getHeaderName())
        title.text = MethodChecker.fromHtml(interlocutorName)
        //    loadAvatar(chatroomViewModel.headerModel.image)
    }

    override fun getInterlocutorName(headerName: String): String = headerName

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        val title = notifier.findViewById<TextView>(R.id.title)
        val action = notifier.findViewById<View>(R.id.action)
        if (isWebSocketError) {
            notifier.show()
            title.setText(com.tokopedia.chat_common.R.string.error_no_connection_retrying)
            action.show()
        } else {
            action.hide()
            notifier.hide()
        }
    }

    override fun removeDynamicStickyButton() {
        var item: DynamicStickyButtonUiModel? = null
        for (it in adapter.list) {
            if (it is DynamicStickyButtonUiModel) {
                item = it
                break
            }
        }

        if (item != null && adapter.list.isNotEmpty()) {
            adapter.clearElement(item)
        }
    }

    override fun handleQuickReplyFromDynamicAttachment(
        toShow: Boolean,
        rejectReasons: DynamicAttachmentRejectReasons
    ) {
        val list = mutableListOf<QuickReplyUiModel>()
        list.addAll(rejectReasons.toQuickReplyUiModel())
        showQuickReply(list, true)
    }

    override fun handleQuickReplyFromDynamicAttachment(
        toShow: Boolean,
        quickReplyUiModel: List<QuickReplyUiModel>
    ) {
        showQuickReply(quickReplyUiModel, true)
    }

    override fun getRecyclerViewId(): Int {
        return R.id.recycler_view
    }

    override fun getNewCommentId(): Int {
        var textAreaUnify = view.findViewById<TextAreaUnify2>(R.id.new_comment)
        return textAreaUnify.editText.id
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

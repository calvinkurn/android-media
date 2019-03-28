package com.tokopedia.topchat.chatroom.view.customview

import android.os.Parcelable
import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.listener.HeaderMenuListener
import com.tokopedia.topchat.chatroom.view.listener.ImagePickerListener
import com.tokopedia.topchat.chatroom.view.listener.SendButtonListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactory
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.util.Utils

/**
 * @author : Steven 29/11/18
 */

class TopChatViewStateImpl(
        @NonNull override val view: View,
        typingListener: TypingListener,
        private val sendListener: SendButtonListener,
        private val templateListener: ChatTemplateListener,
        private val imagePickerListener: ImagePickerListener,
        private val onAttachProductClicked: () -> Unit,
        toolbar: Toolbar,
        val analytics: TopChatAnalytics
) : BaseChatViewStateImpl(view, toolbar, typingListener), TopChatViewState {

    private var attachButton: ImageView = view.findViewById(R.id.add_url)
    private var maximize: View = view.findViewById(R.id.maximize)
    private var templateRecyclerView: RecyclerView = view.findViewById(R.id.list_template)
    private var headerMenuButton: ImageButton = toolbar.findViewById(R.id.header_menu)
    private var chatBlockLayout: View = view.findViewById(R.id.chat_blocked_layout)

    lateinit var templateAdapter: TemplateChatAdapter
    lateinit var templateChatTypeFactory: TemplateChatTypeFactory
    var isUploading: Boolean = false
    var isFirstTime: Boolean = true
    var isShopFollowed: Boolean = false
    lateinit var chatRoomViewModel: ChatroomViewModel

    init {
        initView()
    }

    override fun initView() {
        super.initView()
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }

        maximize.setOnClickListener { maximizeTools() }

        sendButton.setOnClickListener {
            sendListener.onSendClicked(replyEditText.text.toString(),
                    SendableViewModel.generateStartTime())
        }

        templateAdapter = TemplateChatAdapter(TemplateChatTypeFactoryImpl(templateListener))
        templateRecyclerView.setHasFixedSize(true)
        templateRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        templateRecyclerView.adapter = templateAdapter
        templateRecyclerView.visibility = View.GONE

        pickerButton.setOnClickListener {
            analytics.eventPickImage()
            imagePickerListener.pickImageToUpload()
        }

        attachButton.setOnClickListener {
            analytics.eventAttachProduct()
            onAttachProductClicked()
        }
    }

    override fun onSetCustomMessage(customMessage: String) {
        replyEditText.setText(customMessage)
    }

    fun minimizeTools() {
        maximize.visibility = View.VISIBLE
        pickerButton.visibility = View.GONE
        attachButton.visibility = View.GONE
    }

    private fun maximizeTools() {
        maximize.visibility = View.GONE
        pickerButton.visibility = View.VISIBLE
        attachButton.visibility = View.VISIBLE
    }

    fun setDefault() {
        sendButton.requestFocus()
    }

    fun setNonReplyable() {
        actionBox?.visibility = View.GONE

    }

    fun setReplyable() {
        actionBox?.visibility = View.VISIBLE
    }

    override fun getAdapter(): TopChatRoomAdapter {
        return super.getAdapter() as TopChatRoomAdapter
    }

    fun removeDummy(visitable: Visitable<*>) {
        getAdapter().removeDummy(visitable)
    }

    fun addMessage(visitable: Visitable<*>) {
        getAdapter().addNewMessage(visitable)
    }

    fun setActionable(actionable: Boolean) {
        val count = actionBox.childCount
        for (i in 0 until count) {
            actionBox.getChildAt(i).isEnabled = actionable

        }
    }

    fun onSuccessLoadFirstTime(viewModel: ChatroomViewModel,
                               onToolbarClicked: () -> Unit,
                               headerMenuListener: HeaderMenuListener,
                               alertDialog: Dialog,
                               onUnblockChatClicked: () -> Unit) {
        chatRoomViewModel = viewModel
        hideLoading()
        scrollToBottom()
        updateHeader(viewModel, onToolbarClicked)
        showLastTimeOnline(viewModel)
        setHeaderMenuButton(headerMenuListener, alertDialog)
        showReplyBox(viewModel.replyable)
        showActionButtons()
        checkShowQuickReply(viewModel)
        onCheckChatBlocked(viewModel.headerModel.role, viewModel.headerModel.name, viewModel
                .blockedStatus, onUnblockChatClicked)

    }

    private fun showLastTimeOnline(viewModel: ChatroomViewModel) {
        val onlineDesc = toolbar.findViewById<TextView>(R.id.subtitle)
        val onlineStats = toolbar.findViewById<View>(R.id.online_status)

        val string = ChatTimeConverter.getRelativeDate(view.context, viewModel.headerModel.lastTimeOnline)
        onlineDesc.text = string
        onlineDesc.visibility = View.VISIBLE

        if (viewModel.headerModel.label == ChatRoomHeaderViewModel.Companion.TAG_OFFICIAL) {
            onlineStats.visibility = View.GONE
        } else {
            onlineStats.visibility = View.VISIBLE
        }
    }

    private fun setHeaderMenuButton(headerMenuListener: HeaderMenuListener, alertDialog: Dialog) {
        headerMenuButton.visibility = View.VISIBLE
        headerMenuButton.setOnClickListener {
            showHeaderMenuBottomSheet(chatRoomViewModel, headerMenuListener, alertDialog)
        }
    }

    private fun showHeaderMenuBottomSheet(chatroomViewModel: ChatroomViewModel, headerMenuListener: HeaderMenuListener, alertDialog: Dialog) {
        val headerMenu = Menus(view.context)
        val listMenu = ArrayList<Menus.ItemMenus>()

        if (chatroomViewModel.headerModel.role.toLowerCase()
                        .contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP)) {
            val profileText = if (isShopFollowed) {
                view.context.getString(R.string.already_follow_store)
            } else {
                view.context.getString(R.string.follow_store)
            }
            listMenu.add(Menus.ItemMenus(profileText, R.drawable.ic_plus_add))
        }

        listMenu.add(Menus.ItemMenus(view.context.getString(R.string.delete_conversation),
                R.drawable.ic_trash))

        listMenu.add(Menus.ItemMenus(view.context.getString(R.string.chat_incoming_settings), R.drawable
                .ic_chat_settings))

        headerMenu.itemMenuList = listMenu
        headerMenu.setActionText(view.context.getString(R.string.cancel_bottom_sheet))
        headerMenu.setOnActionClickListener { headerMenu.dismiss() }
        headerMenu.setOnItemMenuClickListener { itemMenus, pos ->
            run {
                when {
                    itemMenus.title == view.context.getString(R.string.delete_conversation) -> {
                        showDeleteChatDialog(headerMenuListener, alertDialog)
                    }
                    itemMenus.title == view.context.getString(R.string.follow_store) -> {
                        headerMenuListener.onGoToShop()
                    }
                    itemMenus.title == view.context.getString(R.string.already_follow_store) -> {
                        headerMenuListener.onGoToShop()
                    }
                    itemMenus.title == view.context.getString(R.string.chat_incoming_settings) -> {
                        headerMenuListener.onGoToChatSetting(chatroomViewModel.blockedStatus)
                    }
                    else -> {
                    }
                }
                headerMenu.dismiss()
            }
        }
        headerMenu.show()

    }

    override fun getLastItem(): Parcelable? {
        if (getAdapter().getList().isNotEmpty()) {
            for (i in 0 until getAdapter().getList().size) {
                var item = getAdapter().getList()[i]
                if (item is BaseChatViewModel) {
                    if (item is SendableViewModel) {
                        if ((item as SendableViewModel).isDummy) {
                            break
                        } else {
                            return transform(item as BaseChatViewModel)
                        }
                    } else {
                        return transform(item as BaseChatViewModel)
                    }
                } else {
                    break
                }
            }
        }
        return null
    }

    override fun onCheckChatBlocked(opponentRole: String,
                                    opponentName: String,
                                    blockedStatus: BlockedStatus,
                                    onUnblockChatClicked: () -> Unit) {


        val isBlocked = when {
            opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_OFFICIAL)
            -> {
                blockedStatus.isPromoBlocked
            }
            opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP) -> {
                blockedStatus.isBlocked
            }
            opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_USER) -> {
                blockedStatus.isBlocked
            }
            else -> {
                false
            }
        }

        if (isBlocked) {
            showChatBlocked(blockedStatus, opponentRole, opponentName, onUnblockChatClicked)
        } else {
            removeChatBlocked(blockedStatus)
        }
    }

    private fun showChatBlocked(it: BlockedStatus,
                                opponentRole: String,
                                opponentName: String,
                                onUnblockChatClicked: () -> Unit) {
        updateChatroomBlockedStatus(it)

        showReplyBox(false)
        templateRecyclerView.visibility = View.GONE
        chatBlockLayout.visibility = View.VISIBLE

        setChatBlockedText(chatBlockLayout, it, opponentRole, opponentName)

        val unblockText = chatBlockLayout.findViewById<TextView>(R.id.enable_chat_textView)
        unblockText.setOnClickListener { onUnblockChatClicked() }

    }

    private fun updateChatroomBlockedStatus(it: BlockedStatus) {
        chatRoomViewModel.blockedStatus = it
    }

    private fun setChatBlockedText(chatBlockLayout: View, blockedStatus: BlockedStatus,
                                   opponentRole: String, opponentName: String) {
        val CHAT_PROMOTION = "chat promosi"
        val CHAT_PERSONAL = "chat personal"
        val CHAT_BOTH = "semua chat"

        val blockText = chatBlockLayout.findViewById<TextView>(R.id.blocked_text)
        val category = when {
            opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_OFFICIAL) -> CHAT_PROMOTION
            opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP) ->
                CHAT_BOTH
            opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_USER) ->
                CHAT_PERSONAL
            else -> {
                ""
            }
        }
        val blockString = String.format(
                chatBlockLayout.context.getString(R.string.chat_blocked_text),
                category,
                opponentName,
                Utils.getDateTime(blockedStatus.blockedUntil))

        blockText.text = blockString
    }

    fun removeChatBlocked(it: BlockedStatus) {
        updateChatroomBlockedStatus(it)

        showReplyBox(chatRoomViewModel.replyable)
        templateRecyclerView.visibility = View.VISIBLE
        chatBlockLayout.visibility = View.GONE

    }

    private fun transform(item: BaseChatViewModel): Parcelable? {
        return ReplyParcelableModel(item.messageId, item.message, item.replyTime)
    }

    private fun showDeleteChatDialog(headerMenuListener: HeaderMenuListener, myAlertDialog: Dialog) {
        myAlertDialog.setTitle(view.context.getString(R.string.delete_chat_question))
        myAlertDialog.setDesc(view.context.getString(R.string.delete_chat_warning_message))
        myAlertDialog.setBtnOk(view.context.getString(R.string.delete))
        myAlertDialog.setOnOkClickListener {
            headerMenuListener.onDeleteConversation()
        }
        myAlertDialog.setBtnCancel(view.context.getString(R.string.cancel))
        myAlertDialog.setOnCancelClickListener { myAlertDialog.dismiss() }
        myAlertDialog.show()
    }


    private fun showActionButtons() {
        pickerButton.visibility = View.VISIBLE
        attachProductButton.visibility = View.VISIBLE
        maximizeButton.visibility = View.GONE
    }

    override fun showErrorWebSocket(b: Boolean) {
        notifier.visibility = View.VISIBLE
        val title = notifier.findViewById<TextView>(R.id.title)
        val action = notifier.findViewById<View>(R.id.action)
        if (b) {
            title.setText(R.string.error_no_connection_retrying);
            action.visibility = View.VISIBLE

        } else {
            action.visibility = View.GONE
            notifier.visibility = View.GONE
        }
    }

    private fun checkShowQuickReply(chatroomViewModel: ChatroomViewModel) {
    }

    fun setTemplate(listTemplate: List<Visitable<Any>>?) {
        templateRecyclerView.visibility = View.GONE
        listTemplate?.let {
            templateAdapter.list = listTemplate
            templateRecyclerView.visibility = View.VISIBLE
        }
    }

    fun addTemplateString(message: String) {
        val text = replyEditText.getText().toString()
        val index = replyEditText.getSelectionStart()
        replyEditText.setText(String.format("%s %s %s", text.substring(0, index), message, text
                .substring(index)))
        replyEditText.setSelection(message.length + text.substring(0, index).length + 1)
        analytics.eventClickTemplate()
    }

    override fun showRetryUploadImages(it: ImageUploadViewModel, retry: Boolean) {
        getAdapter().showRetryFor(it, retry)
    }

    fun onSendProductAttachment(item: ProductAttachmentViewModel) {
        getAdapter().addElement(item)
        scrollDownWhenInBottom()
    }

}


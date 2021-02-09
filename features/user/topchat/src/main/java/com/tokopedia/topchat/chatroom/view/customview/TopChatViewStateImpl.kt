package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.widget.LongClickMenu
import com.tokopedia.topchat.chatroom.view.adapter.AttachmentPreviewAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactoryImpl
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuStickerView
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuView
import com.tokopedia.topchat.chatroom.view.listener.HeaderMenuListener
import com.tokopedia.topchat.chatroom.view.listener.ImagePickerListener
import com.tokopedia.topchat.chatroom.view.listener.SendButtonListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.unifycomponents.toPx

/**
 * @author : Steven 29/11/18
 */

class TopChatViewStateImpl constructor(
        @NonNull override val view: View,
        private val typingListener: TypingListener,
        private val sendListener: SendButtonListener,
        private val templateListener: ChatTemplateListener,
        private val imagePickerListener: ImagePickerListener,
        private val attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
        private val stickerMenuListener: ChatMenuStickerView.StickerMenuListener,
        private val headerMenuListener: HeaderMenuListener,
        toolbar: Toolbar,
        val analytics: TopChatAnalytics
) : BaseChatViewStateImpl(view, toolbar, typingListener, attachmentMenuListener),
        TopChatViewState,
        AttachmentPreviewAdapter.AttachmentPreviewListener {

    private var templateRecyclerView: RecyclerView = view.findViewById(R.id.list_template)
    private var headerMenuButton: ImageButton = toolbar.findViewById(com.tokopedia.chat_common.R.id.header_menu)
    private var chatBlockLayout: View = view.findViewById(R.id.chat_blocked_layout)
    private var attachmentPreviewContainer: FrameLayout = view.findViewById(com.tokopedia.chat_common.R.id.cl_attachment_preview)
    private var attachmentPreviewRecyclerView = view.findViewById<RecyclerView>(com.tokopedia.chat_common.R.id.rv_attachment_preview)
    var chatStickerMenuButton: ImageView? = view.findViewById(R.id.iv_chat_sticker)
    var chatMenu: ChatMenuView? = view.findViewById(R.id.fl_chat_menu)

    lateinit var attachmentPreviewAdapter: AttachmentPreviewAdapter
    lateinit var templateAdapter: TemplateChatAdapter
    lateinit var chatRoomViewModel: ChatroomViewModel

    var isShopFollowed: Boolean = false
    var blockStatus: BlockedStatus = BlockedStatus()

    var roomMenu = LongClickMenu()

    override fun getOfflineIndicatorResource() = R.drawable.ic_topchat_status_indicator_offline
    override fun getOnlineIndicatorResource() = R.drawable.ic_topchat_status_indicator_online
    override fun getRecyclerViewId() = R.id.recycler_view
    override fun getNewCommentId() = R.id.new_comment
    override fun getReplyBoxId() = R.id.reply_box
    override fun getActionBoxId() = R.id.add_comment_area
    override fun getSendButtonId() = R.id.send_but
    override fun getNotifierId() = R.id.notifier
    override fun getChatMenuId() = R.id.iv_chat_menu
    override fun getRootViewId() = R.id.main
    override fun shouldShowSellerLabel(): Boolean = false

    init {
        initView()
    }

    override fun getChatRoomHeaderModel(): ChatRoomHeaderViewModel = chatRoomViewModel.headerModel
    override fun useDefaultReplyWatcher(): Boolean = false

    override fun initView() {
        super.initView()
        recyclerView.setHasFixedSize(true)
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }

        templateAdapter = TemplateChatAdapter(TemplateChatTypeFactoryImpl(templateListener))
        templateRecyclerView.setHasFixedSize(true)
        templateRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        templateRecyclerView.adapter = templateAdapter
        templateRecyclerView.hide()

        initProductPreviewLayout()
        initHeaderLayout()
        setupChatStickerMenu()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        getAdapter().addHeaderDateIfDifferent(visitable)
        super.onReceiveMessageEvent(visitable)
    }

    override fun setupChatMenu() {
        chatMenu?.setupAttachmentMenu(attachmentMenuListener)
        chatMenuButton.setOnClickListener {
            chatMenu?.toggleAttachmentMenu()
        }
    }

    private fun setupChatStickerMenu() {
        chatMenu?.setStickerMenuListener(stickerMenuListener)
        chatStickerMenuButton?.setOnClickListener {
            chatMenu?.toggleStickerMenu()
        }
    }

    override fun onStickerOpened() {
        chatStickerMenuButton?.setImageResource(R.drawable.ic_topchat_keyboard)
        chatStickerMenuButton?.setOnClickListener {
            replyEditText.requestFocus()
            chatMenu?.showKeyboard(replyEditText)
        }
    }

    override fun onStickerClosed() {
        chatStickerMenuButton?.setImageResource(R.drawable.ic_topchat_sticker)
        chatStickerMenuButton?.setOnClickListener {
            chatMenu?.toggleStickerMenu()
        }
    }

    override fun setChatBlockStatus(isBlocked: Boolean) {
        blockStatus.isBlocked = isBlocked
    }

    override fun setChatPromoBlockStatus(isBlocked: Boolean, due: String) {
        blockStatus.isPromoBlocked = isBlocked
        blockStatus.blockedUntil = due
    }

    override fun onKeyboardOpened() {
        chatMenu?.isKeyboardOpened = true
        hideChatMenu()
    }

    override fun onKeyboardClosed() {
        chatMenu?.isKeyboardOpened = false
        showChatMenu()
    }

    override fun hideChatMenu() {
        chatMenu?.hideMenu()
    }

    override fun showChatMenu() {
        chatMenu?.showMenuDelayed()
    }

    override fun isAttachmentMenuVisible(): Boolean {
        return chatMenu?.isVisible == true
    }

    override fun hideAttachmentMenu() {
        chatMenu?.hideMenu()
    }

    private fun initHeaderLayout() {
        setupHeaderHamburgerBtn()
    }

    private fun setupHeaderHamburgerBtn() {
        headerMenuButton.setImageResource(R.drawable.ic_topchat_hamburger_menu_grey)
    }

    private fun initProductPreviewLayout() {
        val previewAttachmentFactory = AttachmentPreviewFactoryImpl()
        attachmentPreviewAdapter = AttachmentPreviewAdapter(this, previewAttachmentFactory)
        attachmentPreviewRecyclerView.apply {
            setHasFixedSize(true)
            adapter = attachmentPreviewAdapter
        }
    }

    override fun clearAttachmentPreview() {
        attachmentPreviewAdapter.clear()
        sendListener.onEmptyProductPreview()
        hideProductPreviewLayout()
    }

    override fun hideProductPreviewLayout() {
        attachmentPreviewContainer.hide()
        setChatTemplatesBottomPadding(0)
    }

    private fun setChatTemplatesBottomPadding(bottomPadding: Int) {
        if (!templateRecyclerView.isVisible) return
        addBottomPaddingTemplateChat(bottomPadding)
    }

    private fun addBottomPaddingTemplateChat(bottomPadding: Int) {
        templateRecyclerView.post {
            with(templateRecyclerView) {
                setPadding(
                        paddingLeft,
                        paddingTop,
                        paddingRight,
                        bottomPadding
                )
            }
        }
    }

    override fun onSetCustomMessage(customMessage: String) {
        if (customMessage.isNotEmpty()) {
            replyEditText.setText(customMessage)
        }
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

    fun onSuccessLoadFirstTime(viewModel: ChatroomViewModel,
                               onToolbarClicked: () -> Unit,
                               headerMenuListener: HeaderMenuListener,
                               alertDialog: Dialog) {
        chatRoomViewModel = viewModel
        updateBlockStatus(viewModel)
        scrollToBottom()
        updateHeader(viewModel, onToolbarClicked)
        showLastTimeOnline(viewModel)
        setHeaderMenuButton(headerMenuListener, alertDialog)
        showReplyBox(viewModel.replyable)
        initListPadding(viewModel)
        onCheckChatBlocked(viewModel.headerModel.role, viewModel.headerModel.name, viewModel.blockedStatus)
    }

    private fun initListPadding(viewModel: ChatroomViewModel) {
        if (!viewModel.replyable) {
            val bottomPadding = recyclerView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            recyclerView.setPadding(0, 0, 0, bottomPadding.toInt())
        }
    }

    private fun updateBlockStatus(viewModel: ChatroomViewModel) {
        blockStatus = viewModel.blockedStatus
    }

    override fun updateHeader(chatroomViewModel: ChatroomViewModel, onToolbarClicked: () -> Unit) {
        super.updateHeader(chatroomViewModel, onToolbarClicked)
        bindBadge(chatroomViewModel)
    }

    private fun bindBadge(chatRoom: ChatroomViewModel) {
        val badgeView = toolbar.findViewById<ImageView>(com.tokopedia.chat_common.R.id.ivBadge)
        badgeView?.shouldShowWithAction(chatRoom.hasBadge()) {
            ImageHandler.loadImageWithoutPlaceholder(badgeView, chatRoom.badgeUrl)
        }
    }

    private fun showLastTimeOnline(viewModel: ChatroomViewModel) {
        val onlineDesc = toolbar.findViewById<TextView>(com.tokopedia.chat_common.R.id.subtitle)
        val onlineStats = toolbar.findViewById<View>(com.tokopedia.chat_common.R.id.online_status)
        val lastOnlineTimeStamp = getShopLastTimeOnlineTimeStamp(viewModel)

        if (isOfficialStore(viewModel)) {
            onlineStats.visibility = View.GONE
            onlineDesc.visibility = View.GONE
        } else {
            onlineStats.visibility = View.VISIBLE
            if (lastOnlineTimeStamp != 0L) {
                val onlineDescStatus = getOnlineDescStatus(view.context, viewModel)
                onlineDesc.visibility = View.VISIBLE
                onlineDesc.text = onlineDescStatus
            } else {
                onlineDesc.visibility = View.GONE
            }
        }
    }

    private fun getOnlineDescStatus(context: Context, viewModel: ChatroomViewModel): String {
        return if (viewModel.headerModel.isOnline) {
            context.getString(com.tokopedia.chat_common.R.string.online)
        } else {
            ChatTimeConverter.getRelativeDate(view.context, getShopLastTimeOnlineTimeStamp(viewModel))
        }
    }

    private fun getShopLastTimeOnlineTimeStamp(viewModel: ChatroomViewModel): Long {
        return viewModel.headerModel.lastTimeOnline.toLongOrZero()
    }

    private fun isOfficialStore(viewModel: ChatroomViewModel) = viewModel.headerModel.isOfficialStore()

    private fun setHeaderMenuButton(headerMenuListener: HeaderMenuListener, alertDialog: Dialog) {
        headerMenuButton.visibility = View.VISIBLE
        headerMenuButton.setOnClickListener {
            showHeaderMenuBottomSheet(chatRoomViewModel, headerMenuListener, alertDialog)
        }
    }

    private fun showHeaderMenuBottomSheet(chatroomViewModel: ChatroomViewModel, headerMenuListener: HeaderMenuListener, alertDialog: Dialog) {
        if (roomMenu.isAdded) return
        roomMenu.apply {
            setItemMenuList(createRoomMenu(chatroomViewModel))
            setOnItemMenuClickListener { itemMenus, _ ->
                handleRoomMenuClick(itemMenus, chatroomViewModel, headerMenuListener, alertDialog)
                dismiss()
            }
        }.show(sendListener.getSupportChildFragmentManager(), LongClickMenu.TAG)
    }


    private fun createRoomMenu(userChatRoom: ChatroomViewModel): MutableList<Menus.ItemMenus> {
        val listMenu = ArrayList<Menus.ItemMenus>()

        if (userChatRoom.isChattingWithSeller()) {
            val followStatusMenu = createFollowMenu()
            val promoStatusChanger = createPromoMenu()
            listMenu.add(followStatusMenu)
            listMenu.add(promoStatusChanger)
        }
        val blockChatMenu = createBlockChatMenu()
        listMenu.add(blockChatMenu)
        listMenu.add(Menus.ItemMenus(view.context.getString(R.string.chat_report_user), R.drawable.ic_topchat_report_bold_grey))
        listMenu.add(Menus.ItemMenus(view.context.getString(R.string.delete_conversation), R.drawable.ic_trash_filled_grey))
        return listMenu
    }

    private fun createBlockChatMenu(): Menus.ItemMenus {
        val blockChatStatusTitle: String
        @DrawableRes val blockChatStatusDrawable: Int
        if (blockStatus.isBlocked) {
            blockChatStatusTitle = view.context.getString(R.string.title_unblock_user_chat)
            blockChatStatusDrawable = R.drawable.ic_topchat_unblock_user_chat
        } else {
            blockChatStatusTitle = view.context.getString(R.string.title_block_user_chat)
            blockChatStatusDrawable = R.drawable.ic_topchat_block_user_chat
        }
        return Menus.ItemMenus(blockChatStatusTitle, blockChatStatusDrawable)
    }

    private fun createFollowMenu(): Menus.ItemMenus {
        val followStatusTitle: String
        @DrawableRes val followStatusDrawable: Int
        if (isShopFollowed) {
            followStatusTitle = view.context.getString(R.string.already_follow_store)
            followStatusDrawable = R.drawable.ic_topchat_check_bold_grey
        } else {
            followStatusTitle = view.context.getString(R.string.follow_store)
            followStatusDrawable = R.drawable.ic_topchat_add_bold_grey
        }
        return Menus.ItemMenus(followStatusTitle, followStatusDrawable)
    }

    private fun createPromoMenu(): Menus.ItemMenus {
        val promoStatusTitle: String
        @DrawableRes val promoStatusDrawable: Int
        if (blockStatus.isPromoBlocked) {
            promoStatusTitle = view.context.getString(R.string.title_allow_promo)
            promoStatusDrawable = R.drawable.ic_topchat_allow_promo
        } else {
            promoStatusTitle = view.context.getString(R.string.title_block_promo)
            promoStatusDrawable = R.drawable.ic_topchat_block_promo
        }
        return Menus.ItemMenus(promoStatusTitle, promoStatusDrawable)
    }

    private fun handleRoomMenuClick(
            itemMenus: Menus.ItemMenus,
            chatroomViewModel: ChatroomViewModel,
            headerMenuListener: HeaderMenuListener,
            alertDialog: Dialog
    ) {
        when {
            itemMenus.icon == R.drawable.ic_topchat_unblock_user_chat -> {
                headerMenuListener.unBlockChat()
            }
            itemMenus.icon == R.drawable.ic_topchat_block_user_chat -> {
                showConfirmationBlockChat()
            }
            itemMenus.icon == R.drawable.ic_topchat_allow_promo -> {
                headerMenuListener.onClickAllowPromo()
            }
            itemMenus.icon == R.drawable.ic_topchat_block_promo -> {
                headerMenuListener.onClickBlockPromo()
            }
            itemMenus.title == view.context.getString(R.string.delete_conversation) -> {
                showDeleteChatDialog(headerMenuListener, alertDialog)
            }
            itemMenus.title == view.context.getString(R.string.follow_store) -> {
                headerMenuListener.followUnfollowShop(true)
            }
            itemMenus.title == view.context.getString(R.string.already_follow_store) -> {
                headerMenuListener.followUnfollowShop(false)
            }
            itemMenus.title == view.context.getString(R.string.chat_report_user) -> {
                headerMenuListener.onGoToReportUser()
            }
            else -> {
            }
        }
    }

    override fun showConfirmationBlockChat() {
        val title = view.context.getString(R.string.title_confirm_block_promo)
        val desc = view.context.getString(R.string.desc_confirm_block_promo)
        val titleCtaBlock = view.context.getString(R.string.title_block_user_chat)
        val titleCtaCancel = view.context.getString(R.string.title_block_and_report_user_chat)
        val dialog = DialogUnify(view.context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(title)
            setDescription(desc)
            setPrimaryCTAText(titleCtaBlock)
            setPrimaryCTAClickListener {
                headerMenuListener.blockChat()
                dismiss()
            }
            setSecondaryCTAText(titleCtaCancel)
            setSecondaryCTAClickListener {
                headerMenuListener.blockChat()
                headerMenuListener.onGoToReportUser()
                dismiss()
            }
        }
        dialog.show()
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

    override fun onCheckChatBlocked(
            opponentRole: String,
            opponentName: String,
            blockedStatus: BlockedStatus
    ) {

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
            showChatBlocked(blockedStatus, opponentRole, opponentName)
        } else {
            removeChatBlocked(blockedStatus)
        }
    }

    private fun showChatBlocked(it: BlockedStatus, opponentRole: String, opponentName: String) {
        updateChatroomBlockedStatus(it)

        showReplyBox(false)
        templateRecyclerView.visibility = View.GONE
        chatBlockLayout.visibility = View.VISIBLE

        setChatBlockedText(chatBlockLayout, it, opponentRole, opponentName)

        val unblockText = chatBlockLayout.findViewById<TextView>(R.id.enable_chat_textView)
        unblockText.setOnClickListener { headerMenuListener.unBlockChat() }

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
        val blockString = chatBlockLayout.context.getString(R.string.desc_chat_blocked)
        blockText.text = blockString
    }

    fun removeChatBlocked(it: BlockedStatus) {
        updateChatroomBlockedStatus(it)

        showReplyBox(chatRoomViewModel.replyable)
        templateRecyclerView.showWithCondition(templateAdapter.hasTemplateChat())
        chatBlockLayout.visibility = View.GONE
    }

    private fun transform(item: BaseChatViewModel): Parcelable? {
        return ReplyParcelableModel(item.messageId, item.message, item.replyTime)
    }

    private fun showDeleteChatDialog(headerMenuListener: HeaderMenuListener, myAlertDialog: Dialog) {
        myAlertDialog.setTitle(view.context.getString(R.string.delete_chat_question))
        myAlertDialog.setDesc(view.context.getString(R.string.delete_chat_warning_message))
        myAlertDialog.setBtnOk(view.context.getString(R.string.topchat_chat_delete_confirm))
        myAlertDialog.setOnOkClickListener {
            headerMenuListener.onDeleteConversation()
        }
        myAlertDialog.setBtnCancel(view.context.getString(com.tokopedia.resources.common.R.string.general_label_cancel))
        myAlertDialog.setOnCancelClickListener { myAlertDialog.dismiss() }
        myAlertDialog.show()
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        notifier.visibility = View.VISIBLE
        val title = notifier.findViewById<TextView>(R.id.title)
        val action = notifier.findViewById<View>(R.id.action)
        if (isWebSocketError) {
            title.setText(R.string.error_no_connection_retrying);
            action.visibility = View.VISIBLE

        } else {
            action.visibility = View.GONE
            notifier.visibility = View.GONE
        }
    }

    override fun updateTemplateState() {
        if (!templateRecyclerView.isVisible && templateAdapter.hasTemplateChat()) {
            showTemplateChat()
        }
    }

    fun setTemplate(
            listTemplate: List<Visitable<Any>>?,
            isLastMessageBroadcast: Boolean = false,
            amIBuyer: Boolean = true
    ) {
        val isLastMsgFromBroadcastAndIamBuyer = isLastMessageBroadcast && amIBuyer
        templateRecyclerView.visibility = View.GONE
        listTemplate?.let {
            templateAdapter.list = listTemplate
            if (templateAdapter.hasTemplateChat() && !isLastMsgFromBroadcastAndIamBuyer) {
                showTemplateChat()
            } else {
                hideTemplateChat()
            }
        }
    }

    private fun showTemplateChat() {
        templateRecyclerView.show()
        addBottomPaddingTemplateChat(8.toPx())
    }

    private fun hideTemplateChat() {
        templateRecyclerView.hide()
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

    override fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>) {
        attachmentPreviewContainer.show()
        setChatTemplatesBottomPadding(8.toPx())
        attachmentPreviewAdapter.updateAttachments(attachmentPreview)
    }

    override fun focusOnReply() {
        replyEditText.requestFocus()
    }

}

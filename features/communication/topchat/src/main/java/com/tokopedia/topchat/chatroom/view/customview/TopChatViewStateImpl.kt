package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.os.Parcelable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.view.widget.LongClickMenu
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.view.adapter.AttachmentPreviewAdapter
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactoryImpl
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuStickerView
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuView
import com.tokopedia.topchat.chatroom.view.custom.ChatTextAreaTabLayout
import com.tokopedia.topchat.chatroom.view.custom.ChatTextAreaTabLayoutListener
import com.tokopedia.topchat.chatroom.view.listener.HeaderMenuListener
import com.tokopedia.topchat.chatroom.view.listener.SendButtonListener
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.uimodel.ReplyParcelableModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt
import com.tokopedia.topchat.common.data.TopchatItemMenu
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_ALLOW_PROMO
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_BLOCK_CHAT
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_BLOCK_PROMO
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_CHAT_SETTING
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_DELETE_CHAT
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_FOLLOW
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_REPORT_USER
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_UNBLOCK_CHAT
import com.tokopedia.topchat.common.data.TopchatItemMenu.Companion.ID_UNFOLLOW
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.util.Locale

/**
 * @author : Steven 29/11/18
 */

open class TopChatViewStateImpl constructor(
    @NonNull override val view: View,
    private val typingListener: TypingListener,
    protected val sendListener: SendButtonListener,
    private val templateListener: ChatTemplateListener,
    private val attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
    private val stickerMenuListener: ChatMenuStickerView.StickerMenuListener,
    private val headerMenuListener: HeaderMenuListener,
    private val chatTextAreaTabLayoutListener: ChatTextAreaTabLayoutListener,
    toolbar: Toolbar,
    val analytics: TopChatAnalytics,
    private val userSession: UserSessionInterface
) : BaseChatViewStateImpl(view, toolbar, typingListener, attachmentMenuListener),
    TopChatViewState,
    AttachmentPreviewAdapter.AttachmentPreviewListener {

    private var templateRecyclerView: RecyclerView = view.findViewById(R.id.list_template)
    private var headerMenuButton: ImageButton = toolbar.findViewById(com.tokopedia.chat_common.R.id.header_menu)
    private var chatBlockLayout: View = view.findViewById(R.id.chat_blocked_layout)
    private var attachmentPreviewContainer: LinearLayout = view.findViewById(R.id.cl_attachment_preview)
    private var attachmentPreviewRecyclerView = view.findViewById<RecyclerView>(R.id.rv_attachment_preview)
    var chatStickerMenuButton: IconUnify? = view.findViewById(R.id.iv_chat_sticker)
    var chatMenu: ChatMenuView? = view.findViewById(R.id.fl_chat_menu)
    private var userStatus: Typography? = null
    private var typingImage: ImageUnify? = null
    private var typingText: Typography? = null
    private var fragmentView: TopChatContract.View? = null

    lateinit var attachmentPreviewAdapter: AttachmentPreviewAdapter
    lateinit var templateAdapter: TemplateChatAdapter
    lateinit var chatRoomViewModel: ChatroomViewModel

    var isShopFollowed: Boolean = false
    var blockStatus: BlockedStatus = BlockedStatus()

    var chatTextAreaTabLayout: ChatTextAreaTabLayout? = null
    private var chatTextAreaShimmer: LoaderUnify? = null

    var isAbleToReply: Boolean? = null
        set(value) {
            field = value
            showReplyBox()
        }

    var shouldShowSrw: Boolean? = null
        set(value) {
            field = value
            showReplyBox()
        }

    var roomMenu = LongClickMenu()

    override fun getOfflineIndicatorResource() = R.drawable.ic_topchat_status_indicator_offline
    override fun getOnlineIndicatorResource() = R.drawable.ic_topchat_status_indicator_online
    override fun getRecyclerViewId() = R.id.recycler_view_chatroom
    override fun getNewCommentId() = R.id.new_comment
    override fun getReplyBoxId() = R.id.reply_box
    override fun getActionBoxId() = R.id.add_comment_area
    override fun getSendButtonId() = R.id.send_but
    override fun getNotifierId() = R.id.notifier
    override fun getChatMenuId() = R.id.iv_chat_menu
    override fun getAttachmentMenuId(): Int = View.NO_ID
    override fun getAttachmentMenuContainer(): Int = View.NO_ID
    override fun getRootViewId() = R.id.main

    override fun shouldShowSellerLabel(): Boolean = false

    init {
        initView()
    }

    override fun getChatRoomHeaderModel(): ChatRoomHeaderUiModel = chatRoomViewModel.headerModel
    override fun useDefaultReplyWatcher(): Boolean = false

    override fun initView() {
        super.initView()
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null
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

        userStatus = toolbar.findViewById(com.tokopedia.chat_common.R.id.subtitle)
        typingImage = toolbar.findViewById(com.tokopedia.chat_common.R.id.iv_typing)
        typingText = toolbar.findViewById(com.tokopedia.chat_common.R.id.tv_typing)

        typingImage?.let {
            ImageUtil.setTypingAnimation(it)
        }
        initProductPreviewLayout()
        initHeaderLayout()
        setupChatStickerMenu()

        chatTextAreaTabLayout = view.findViewById(R.id.layout_chat_text_area)
        chatTextAreaTabLayout?.setupListener(chatTextAreaTabLayoutListener)
        chatTextAreaShimmer = view.findViewById(R.id.chat_area_shimmer)
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        getAdapter().addHeaderDateIfDifferent(visitable)
        super.onReceiveMessageEvent(visitable)
    }

    override fun setupChatMenu() {
        chatMenu?.setupAttachmentMenu(attachmentMenuListener)
        chatMenuButton.setOnClickListener {
            if (isFromBubble) {
                TopChatAnalyticsKt.clickAddAttachmentFromBubble(userSession.shopId)
            }
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
        if (isChatTabVisible()) {
            chatTextAreaTabLayout?.onStickerOpened()
        } else {
            chatStickerMenuButton?.setImage(IconUnify.KEYBOARD)
            chatStickerMenuButton?.setOnClickListener {
                replyEditText.requestFocus()
                chatMenu?.showKeyboard(replyEditText)
            }
        }
    }

    override fun onStickerClosed() {
        if (isChatTabVisible()) {
            chatTextAreaTabLayout?.onStickerClosed()
        } else {
            chatStickerMenuButton?.setImage(IconUnify.STICKER)
            chatStickerMenuButton?.setOnClickListener {
                chatMenu?.toggleStickerMenu()
            }
        }
    }

    fun isChatTabVisible(): Boolean {
        return chatTextAreaTabLayout?.visibility == View.VISIBLE
    }

    override fun setChatBlockStatus(isBlocked: Boolean) {
        blockStatus.isBlocked = isBlocked
    }

    override fun setChatPromoBlockStatus(isBlocked: Boolean, due: String) {
        blockStatus.isPromoBlocked = isBlocked
        blockStatus.blockedUntil = due
    }

    override fun onKeyboardOpened() {
        if (chatMenu?.isKeyboardOpened == false) {
            chatMenu?.isKeyboardOpened = true
            hideChatMenu()
            fragmentView?.collapseSrw()
        }

        /**
         * Keyboard Handler for new SRW & Tab Design menu
         */
        if (chatTextAreaTabLayout?.chatMenu?.isKeyboardOpened == false) {
            chatTextAreaTabLayout?.chatMenu?.isKeyboardOpened = true
            chatTextAreaTabLayout?.chatMenu?.hideMenu()
        }
    }

    override fun onKeyboardClosed() {
        if (chatMenu?.isKeyboardOpened == true) {
            chatMenu?.isKeyboardOpened = false
            fragmentView?.expandSrw()
            showChatMenu()
        }

        /**
         * Keyboard Handler for new SRW & Tab Design menu
         */
        if (chatTextAreaTabLayout?.chatMenu?.isKeyboardOpened == true) {
            chatTextAreaTabLayout?.chatMenu?.isKeyboardOpened = false
            chatTextAreaTabLayout?.chatMenu?.showMenuDelayed()
        }
    }

    override fun clearEditText() {
        super.clearEditText()
        chatTextAreaTabLayout?.replyEditText?.setText("")
    }

    override fun isKeyboardOpen(): Boolean {
        return chatMenu?.isKeyboardOpened == true
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
        fragmentView?.updateSrwPreviewState()
        fragmentView?.expandSrwBubble()
    }

    override fun hideProductPreviewLayout() {
        attachmentPreviewContainer.hide()
    }

    override fun notifyPreviewRemoved(model: SendablePreview) {
        fragmentView?.notifyPreviewRemoved(model)
    }

    override fun reloadCurrentAttachment() {
        fragmentView?.reloadCurrentAttachment()
    }

    override fun onSetCustomMessage(customMessage: String) {
        if (customMessage.isNotEmpty()) {
            replyEditText.setText(customMessage)
        }
    }

    override fun getAdapter(): TopChatRoomAdapter {
        return super.getAdapter() as TopChatRoomAdapter
    }

    fun onSuccessLoadFirstTime(
        viewModel: ChatroomViewModel,
        onToolbarClicked: () -> Unit,
        headerMenuListener: HeaderMenuListener
    ) {
        chatRoomViewModel = viewModel
        updateBlockStatus(viewModel)
        scrollToBottom()
        updateHeader(viewModel, onToolbarClicked)
        showLastTimeOnline(viewModel)
        setHeaderMenuButton(headerMenuListener)
        isAbleToReply = viewModel.replyable
        initListPadding(viewModel)
        onCheckChatBlocked(viewModel.headerModel.role, viewModel.headerModel.name, viewModel.blockedStatus)
    }

    override fun scrollToBottom() {
        recyclerView.scrollToPosition(0)
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
            badgeView.loadImageWithoutPlaceholder(chatRoom.badgeUrl)
        }
    }

    private fun showLastTimeOnline(viewModel: ChatroomViewModel) {
        val onlineDesc = toolbar.findViewById<TextView>(com.tokopedia.chat_common.R.id.subtitle)
        val onlineStats = toolbar.findViewById<View>(com.tokopedia.chat_common.R.id.online_status)
        val lastOnlineTimeStamp = getShopLastTimeOnlineTimeStamp(viewModel)

        if (isOfficialAccountTokopedia(viewModel)) {
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

    private fun isOfficialAccountTokopedia(viewModel: ChatroomViewModel) =
        viewModel.headerModel.isOfficialAccountTokopedia()

    private fun setHeaderMenuButton(
        headerMenuListener: HeaderMenuListener
    ) {
        headerMenuButton.visibility = View.VISIBLE
        headerMenuButton.setOnClickListener {
            showHeaderMenuBottomSheet(
                chatRoomViewModel,
                headerMenuListener
            )
            headerMenuListener.onClickHeaderMenu()
        }
    }

    protected open fun showHeaderMenuBottomSheet(
        chatroomViewModel: ChatroomViewModel,
        headerMenuListener: HeaderMenuListener
    ) {
        if (roomMenu.isAdded) return
        if (isFromBubble) {
            KeyboardHandler.DropKeyboard(view.context, view)
        }
        roomMenu.apply {
            setItemMenuList(createRoomMenu(chatroomViewModel))
            setOnItemMenuClickListener { itemMenus, _ ->
                handleRoomMenuClick(
                    itemMenus,
                    chatroomViewModel,
                    headerMenuListener
                )
                dismiss()
            }
        }.show(sendListener.getSupportChildFragmentManager(), LongClickMenu.TAG)
    }

    private fun createRoomMenu(userChatRoom: ChatroomViewModel): MutableList<TopchatItemMenu> {
        val listMenu = ArrayList<TopchatItemMenu>()

        if (userChatRoom.isChattingWithSeller()) {
            listMenu.add(createFollowMenu())
            listMenu.add(createPromoMenu())
        } else {
            listMenu.add(createChatSettingMenu())
        }

        if (!isOfficialAccountTokopedia(userChatRoom)) {
            listMenu.add(createBlockChatMenu())
            listMenu.add(createReportUserMenu())
        }
        listMenu.add(createDeleteChatMenu())
        return listMenu
    }

    private fun createChatSettingMenu(): TopchatItemMenu {
        return TopchatItemMenu(
            title = view.context.getString(R.string.title_chat_setting),
            iconUnify = IconUnify.CHAT_SETTING,
            id = ID_CHAT_SETTING,
            showNewLabel = true
        )
    }

    private fun createReportUserMenu(): TopchatItemMenu {
        return TopchatItemMenu(
            title = view.context.getString(R.string.chat_report_user),
            iconUnify = IconUnify.USER_REPORT,
            id = ID_REPORT_USER
        )
    }

    private fun createDeleteChatMenu(): TopchatItemMenu {
        return TopchatItemMenu(
            title = view.context.getString(R.string.delete_conversation),
            iconUnify = IconUnify.DELETE,
            id = ID_DELETE_CHAT
        )
    }

    private fun createBlockChatMenu(): TopchatItemMenu {
        val blockChatStatusTitle: String
        val blockChatStatusDrawable: Int
        val id: Int
        if (blockStatus.isBlocked) {
            blockChatStatusTitle = view.context.getString(R.string.title_unblock_user_chat)
            blockChatStatusDrawable = IconUnify.USER_SUCCESS
            id = ID_UNBLOCK_CHAT
        } else {
            blockChatStatusTitle = view.context.getString(R.string.title_block_user_chat)
            blockChatStatusDrawable = IconUnify.USER_BLOCK
            id = ID_BLOCK_CHAT
        }
        return TopchatItemMenu(
            title = blockChatStatusTitle,
            iconUnify = blockChatStatusDrawable,
            id = id
        )
    }

    private fun createFollowMenu(): TopchatItemMenu {
        val followStatusTitle: String
        val followStatusDrawable: Int
        val id: Int
        if (isShopFollowed) {
            followStatusTitle = view.context.getString(R.string.already_follow_store)
            followStatusDrawable = IconUnify.CHECK_BIG
            id = ID_UNFOLLOW
        } else {
            followStatusTitle = view.context.getString(R.string.follow_store)
            followStatusDrawable = IconUnify.ADD
            id = ID_FOLLOW
        }
        return TopchatItemMenu(
            title = followStatusTitle,
            iconUnify = followStatusDrawable,
            id = id
        )
    }

    private fun createPromoMenu(): TopchatItemMenu {
        val promoStatusTitle: String
        val promoStatusDrawable: Int
        val id: Int
        if (blockStatus.isPromoBlocked) {
            promoStatusTitle = view.context.getString(R.string.title_allow_promo)
            promoStatusDrawable = IconUnify.PROMO
            id = ID_ALLOW_PROMO
        } else {
            promoStatusTitle = view.context.getString(R.string.title_block_promo)
            promoStatusDrawable = IconUnify.PROMO_BLOCK
            id = ID_BLOCK_PROMO
        }
        return TopchatItemMenu(
            title = promoStatusTitle,
            iconUnify = promoStatusDrawable,
            id = id
        )
    }

    private fun handleRoomMenuClick(
        itemMenus: TopchatItemMenu,
        chatroomViewModel: ChatroomViewModel,
        headerMenuListener: HeaderMenuListener
    ) {
        when (itemMenus.id) {
            ID_UNBLOCK_CHAT -> headerMenuListener.unBlockChat()
            ID_BLOCK_CHAT -> showConfirmationBlockChat()
            ID_ALLOW_PROMO -> headerMenuListener.onClickAllowPromo()
            ID_BLOCK_PROMO -> headerMenuListener.onClickBlockPromo()
            ID_DELETE_CHAT -> showDeleteChatDialog(headerMenuListener)
            ID_FOLLOW -> headerMenuListener.followUnfollowShop(true)
            ID_UNFOLLOW -> headerMenuListener.followUnfollowShop(false)
            ID_REPORT_USER -> headerMenuListener.onGoToReportUser()
            ID_CHAT_SETTING -> headerMenuListener.onGoToChatSetting()
        }

        headerMenuListener.onClickHeaderMenuItem(itemMenus.title)
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
                if (item is BaseChatUiModel) {
                    if (item is SendableUiModel) {
                        if ((item as SendableUiModel).isDummy) {
                            break
                        } else {
                            return transform(item as BaseChatUiModel)
                        }
                    } else {
                        return transform(item as BaseChatUiModel)
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
            opponentRole.lowercase(Locale.getDefault())
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_OFFICIAL)
            -> {
                blockedStatus.isPromoBlocked
            }
            opponentRole.lowercase(Locale.getDefault())
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_SHOP) -> {
                blockedStatus.isBlocked
            }
            opponentRole.lowercase(Locale.getDefault())
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_USER) -> {
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

        if (isAbleToReply == null || isAbleToReply == true) {
            isAbleToReply = false
        }
        templateRecyclerView.visibility = View.GONE
        chatBlockLayout.visibility = View.VISIBLE

        setChatBlockedText(chatBlockLayout, it, opponentRole, opponentName)

        val unblockText = chatBlockLayout.findViewById<TextView>(R.id.enable_chat_textView)
        unblockText.setOnClickListener { headerMenuListener.unBlockChat() }
    }

    private fun updateChatroomBlockedStatus(it: BlockedStatus) {
        chatRoomViewModel.blockedStatus = it
    }

    private fun setChatBlockedText(
        chatBlockLayout: View,
        blockedStatus: BlockedStatus,
        opponentRole: String,
        opponentName: String
    ) {
        val CHAT_PROMOTION = "chat promosi"
        val CHAT_PERSONAL = "chat personal"
        val CHAT_BOTH = "semua chat"

        val blockText = chatBlockLayout.findViewById<TextView>(R.id.blocked_text)
        val category = when {
            opponentRole.lowercase(Locale.getDefault())
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_OFFICIAL) -> CHAT_PROMOTION
            opponentRole.lowercase(Locale.getDefault())
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_SHOP) ->
                CHAT_BOTH
            opponentRole.lowercase(Locale.getDefault())
                .contains(ChatRoomHeaderUiModel.Companion.ROLE_USER) ->
                CHAT_PERSONAL
            else -> {
                ""
            }
        }
        val blockString = chatBlockLayout.context.getString(R.string.desc_chat_blocked)
        blockText.text = blockString
    }

    private fun removeChatBlocked(it: BlockedStatus) {
        updateChatroomBlockedStatus(it)

        if (isAbleToReply == null || isAbleToReply != chatRoomViewModel.replyable) {
            isAbleToReply = chatRoomViewModel.replyable
        }
        templateRecyclerView.showWithCondition(templateAdapter.hasTemplateChat())
        chatBlockLayout.visibility = View.GONE
    }

    private fun transform(item: BaseChatUiModel): Parcelable {
        return ReplyParcelableModel(item.messageId, item.message, item.replyTime ?: "")
    }

    private fun showDeleteChatDialog(
        headerMenuListener: HeaderMenuListener
    ) {
        view.context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.delete_chat_question))
                setDescription(it.getString(R.string.delete_chat_warning_message))
                setSecondaryCTAText(
                    it.getString(
                        com.tokopedia.resources.common.R.string.general_label_cancel
                    )
                )
                setPrimaryCTAText(it.getString(R.string.topchat_chat_delete_confirm))
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setPrimaryCTAClickListener {
                    headerMenuListener.onDeleteConversation()
                    dismiss()
                }
            }.show()
        }
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {
        notifier.visibility = View.VISIBLE
        val title = notifier.findViewById<TextView>(R.id.title)
        val action = notifier.findViewById<View>(R.id.action)
        if (isWebSocketError) {
            title.setText(R.string.error_no_connection_retrying)
            action.visibility = View.VISIBLE
        } else {
            action.visibility = View.GONE
            notifier.visibility = View.GONE
        }
    }

    override fun hasProductPreviewShown(): Boolean {
        return hasVisibleSendablePreview() && attachmentPreviewAdapter.isShowingProduct()
    }

    override fun hasVisibleSendablePreview(): Boolean {
        return attachmentPreviewContainer.isVisible
    }

    override fun showTemplateChatIfReady(
        lastMessageBroadcast: Boolean,
        lastMessageSrwBubble: Boolean,
        amIBuyer: Boolean
    ) {
        if (isTemplateReady(lastMessageBroadcast, lastMessageSrwBubble, amIBuyer)) {
            showTemplateChat()
        }
    }

    private fun isTemplateReady(
        lastMessageBroadcast: Boolean,
        lastMessageSrwBubble: Boolean,
        amIBuyer: Boolean,
        separatedTemplateVisible: Boolean = false
    ): Boolean {
        val isLastMsgFromBroadcastAndIamBuyer = lastMessageBroadcast && amIBuyer
        return !templateRecyclerView.isVisible &&
            templateAdapter.hasTemplateChat() &&
            !isLastMsgFromBroadcastAndIamBuyer &&
            fragmentView?.shouldShowSrw() == false &&
            !lastMessageSrwBubble &&
            !separatedTemplateVisible
    }

    override fun attachFragmentView(fragmentView: TopChatContract.View) {
        this.fragmentView = fragmentView
    }

    override fun hideKeyboard() {
        chatMenu?.hideKeyboard()
    }

    fun setTemplate(
        listTemplate: List<Visitable<*>>?,
        isLastMessageBroadcast: Boolean = false,
        amIBuyer: Boolean = true
    ) {
        templateRecyclerView.visibility = View.GONE
        listTemplate?.let {
            templateAdapter.setList(listTemplate)
            if (setTemplateChecker(isLastMessageBroadcast, amIBuyer)) {
                showTemplateChat()
            } else {
                hideTemplateChat()
            }
        }
    }

    private fun setTemplateChecker(
        isLastMessageBroadcast: Boolean,
        amIBuyer: Boolean,
        separatedTemplateVisible: Boolean = false
    ): Boolean {
        val isLastMsgFromBroadcastAndIamBuyer = isLastMessageBroadcast && amIBuyer
        return templateAdapter.hasTemplateChat() &&
            !isLastMsgFromBroadcastAndIamBuyer &&
            (
                fragmentView?.hasProductPreviewShown() == false ||
                    fragmentView?.hasNoSrw() == true
                ) &&
            !separatedTemplateVisible
    }

    fun showTemplateChat() {
        templateRecyclerView.show()
    }

    fun hideTemplateChat() {
        templateRecyclerView.hide()
    }

    fun addTemplateString(message: String) {
        val text = replyEditText.getText().toString()
        val index = replyEditText.getSelectionStart()
        replyEditText.setText(
            String.format(
                Locale.getDefault(),
                "%s %s %s",
                text.substring(0, index),
                message,
                text.substring(index)
            )
        )
        replyEditText.setSelection(message.length + text.substring(0, index).length + 1)
        analytics.eventClickTemplate()
    }

    override fun showRetryUploadImages(it: ImageUploadUiModel, retry: Boolean) {
        getAdapter().showRetryFor(it, retry)
    }

    fun updateProductPreviews(mapProducts: ArrayMap<String, Attachment>) {
        val listProductPreview = arrayListOf<Attachment>()
        mapProducts.forEach {
            listProductPreview.add(it.value)
        }
        attachmentPreviewAdapter.updateDeferredAttachment(listProductPreview)
    }

    override fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>) {
        attachmentPreviewContainer.show()
        attachmentPreviewAdapter.updateAttachments(attachmentPreview)
    }

    override fun focusOnReply() {
        replyEditText.requestFocus()
    }

    override fun onShowStartTyping() {
        userStatus?.hide()
        typingImage?.show()
        typingText?.show()
        typingImage?.let {
            ImageUtil.startAVDTypingAnimation(it)
        }
    }

    override fun onShowStopTyping() {
        userStatus?.show()
        typingImage?.hide()
        typingText?.hide()
        typingImage?.let {
            ImageUtil.stopAVDTypingAnimation(it)
        }
    }

    private fun showReplyBox() {
        /**
         * Check if GetExistingChat & SRW finished
         */
        if (isAbleToReply != null && shouldShowSrw != null) {
            when {
                (isAbleToReply == true) && (shouldShowSrw == true) -> {
                    // Hide Shimmer, show comment area, hide reply box, show tab
                    chatTextAreaShimmer?.hide()
                    actionBox.show()
                    replyBox.hide()
                    // Render SRW only when success get the questions & active tab is SRW
                    if (chatTextAreaTabLayout?.srwLayout?.isSuccessState() == true &&
                        chatTextAreaTabLayout?.tabState ==
                        ChatTextAreaTabLayout.TabLayoutActiveStatus.SRW
                    ) {
                        chatTextAreaTabLayout?.srwLayout?.renderSrwState()
                    }
                    chatTextAreaTabLayout?.show()
                    hideKeyboard()
                }
                (isAbleToReply == true) && (shouldShowSrw == false) -> {
                    // Hide Shimmer, show comment area, show reply box, hide tab
                    chatTextAreaShimmer?.hide()
                    actionBox.show()
                    replyBox.show()
                    chatTextAreaTabLayout?.hide()
                }
                (isAbleToReply == false) -> {
                    // Hide Shimmer, hide comment area, hide reply box, hide tab
                    chatTextAreaShimmer?.hide()
                    actionBox.hide()
                    replyBox.hide()
                    chatTextAreaTabLayout?.hide()
                }
                else -> {
                    // Show Shimmer, hide comment area, hide reply box, hide tab
                    showChatAreaShimmer()
                }
            }
        }
    }

    fun showChatAreaShimmer() {
        chatTextAreaShimmer?.show()
        actionBox.hide()
        replyBox.hide()
        chatTextAreaTabLayout?.hide()
    }

    fun getChatAreaShimmer(): LoaderUnify? {
        return chatTextAreaShimmer
    }
}

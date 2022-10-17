package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.topchat.chatroom.view.listener.ReplyBoxTextListener
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifyprinciples.Typography

class ChatTextAreaTabLayout: ConstraintLayout {

    /**
     * Tab SRW
     */
    private var tabSRW: View? = null
    private var textTabSRW: Typography? = null
    var srwLayout : SrwFrameLayout? = null

    /**
     * Tab Tulis Pesan
     */
    var tabReplyBox: View? = null
    private var textTabReplyBox: Typography? = null
    private var replyBoxLayout : ComposeMessageAreaConstraintLayout? = null
    var replyEditText: EditText? = null
    private var sendButton: IconUnify? = null

    /**
     * Tab background
     */
    private var tabBackgroundActive: Drawable? = null
    private var tabBackgroundInactive: Drawable? = null

    /**
     * Chat Menu
     */
    var chatMenu: ChatMenuView? = null
    private var chatMenuButton: ImageView? = null
    private var chatStickerMenuButton: IconUnify? = null

    /**
     * List of tab-layout
     */
    private var tabList = hashMapOf<TabLayoutActiveStatus, TabView>()
    var tabState: TabLayoutActiveStatus = TabLayoutActiveStatus.SRW

    /**
     * Listener
     */
    private var listener: ChatTextAreaTabLayoutListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        initViewBind()
        initListener()
        initBackground()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBind() {
        tabSRW = findViewById(R.id.srw_tab_bg)
        textTabSRW = findViewById(R.id.tv_srw_tab)
        srwLayout = findViewById(R.id.rv_srw_tab)

        tabReplyBox = findViewById(R.id.textarea_tab_bg)
        textTabReplyBox = findViewById(R.id.tv_textarea_tab)
        replyBoxLayout = findViewById(R.id.reply_box_tab)
        chatMenu = findViewById(R.id.chat_menu_view_tab)
        chatMenuButton = findViewById(R.id.iv_chat_menu)
        chatStickerMenuButton = findViewById(R.id.iv_chat_sticker)
        replyEditText = findViewById(R.id.new_comment)
        sendButton = findViewById(R.id.send_but)

        /**
         * Add new tab here
         */
        tabList.apply {
            val tabSRW = TabView(
                tabLayout = srwLayout,
                tabBackground = tabSRW,
                tabText = textTabSRW
            )
            this[TabLayoutActiveStatus.SRW] = tabSRW

            val tabReplyBox = TabView(
                tabLayout = replyBoxLayout,
                tabBackground = tabReplyBox,
                tabText = textTabReplyBox
            )
            this[TabLayoutActiveStatus.ReplyBox] = tabReplyBox
        }
    }

    private fun initBackground() {
        tabBackgroundActive = ViewUtil.generateBackgroundWithShadow(
            this,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            topLeftRadius = R.dimen.dp_topchat_8,
            topRightRadius = R.dimen.dp_topchat_8,
            bottomLeftRadius = R.dimen.dp_topchat_8,
            bottomRightRadius = R.dimen.dp_topchat_8,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            elevation = R.dimen.dp_topchat_0,
            shadowRadius = R.dimen.dp_topchat_0,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_NN50,
            strokeWidth = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER
        )
        tabBackgroundInactive = ViewUtil.generateBackgroundWithShadow(
            this,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_NN100,
            topLeftRadius = R.dimen.dp_topchat_8,
            topRightRadius = R.dimen.dp_topchat_8,
            bottomLeftRadius = R.dimen.dp_topchat_8,
            bottomRightRadius = R.dimen.dp_topchat_8,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            elevation = R.dimen.dp_topchat_0,
            shadowRadius = R.dimen.dp_topchat_0,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_NN50,
            strokeWidth = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER
        )

        tabSRW?.background = tabBackgroundActive
        tabReplyBox?.background = tabBackgroundInactive

    }

    private fun initListener() {
        tabSRW?.setOnClickListener {
            onTabClicked(
                clickedTab = TabLayoutActiveStatus.SRW,
                additional = {
                    listener?.onClickSRWTab()
                    chatMenu?.hideKeyboard()
                }
            )
        }
        tabReplyBox?.setOnClickListener {
            onTabClicked(
                clickedTab = TabLayoutActiveStatus.ReplyBox,
                additional = {
                    listener?.onClickReplyTab()
                    replyEditText?.requestFocus()
                    chatMenu?.showKeyboard(replyEditText)
                }
            )
        }
    }

    private fun onTabClicked(
        clickedTab: TabLayoutActiveStatus,
        additional: () -> Unit
    ) {
        changeToInactive()
        tabState = clickedTab
        updateTab()
        additional()
    }

    fun initSrw(
        srwQuestionListener: SrwQuestionViewHolder.Listener,
        srwFrameLayoutListener: SrwFrameLayout.Listener
    ) {
        srwLayout?.initialize(srwQuestionListener, srwFrameLayoutListener)
        srwLayout?.setSrwTitleVisibility(false)
        srwLayout?.setContentMargin()
        srwLayout?.isSrwBubble = false
        srwLayout?.onBoardingAnchor = tabSRW
    }

    fun initReplyBox(
        typingListener: TypingListener,
        replyBoxTextListener: ReplyBoxTextListener,
        attachmentMenuListener: AttachmentMenu.AttachmentMenuListener,
        stickerMenuListener: ChatMenuStickerView.StickerMenuListener
    ) {
        replyBoxLayout?.initLayout(typingListener, replyBoxTextListener)
        initAttachmentMenu(attachmentMenuListener)
        initStickerMenu(stickerMenuListener)
    }

    private fun initAttachmentMenu(attachmentMenuListener: AttachmentMenu.AttachmentMenuListener) {
        chatMenu?.setupAttachmentMenu(attachmentMenuListener)
        chatMenuButton?.setOnClickListener {
            chatMenu?.toggleAttachmentMenu()
        }
    }

    private fun initStickerMenu(stickerMenuListener: ChatMenuStickerView.StickerMenuListener) {
        chatMenu?.setStickerMenuListener(stickerMenuListener)
        chatStickerMenuButton?.setOnClickListener {
            chatMenu?.toggleStickerMenu()
        }
    }

    private fun updateTab() {
        chatMenu?.hideMenu()
        changeTabState(tabList[tabState]?.tabBackground, isActive = true)
        showTabContent()
    }

    private fun changeTabState(pickedView: View?, isActive: Boolean) {
        pickedView?.let {
            if (isActive) {
                it.background = tabBackgroundActive
            } else {
                it.background = tabBackgroundInactive
            }
        }
    }

    private fun showTabContent() {
        val tabView = tabList[tabState]
        tabView?.tabLayout?.show()
        tabView?.tabBackground?.show()
        tabView?.tabText?.show()
    }

    private fun changeToInactive() {
        val tabView = tabList[tabState]
        changeTabState(tabView?.tabBackground, isActive = false)
        tabView?.tabLayout?.hide()
    }

    fun onStickerOpened() {
        chatStickerMenuButton?.setImage(IconUnify.KEYBOARD)
        chatStickerMenuButton?.setOnClickListener {
            replyEditText?.requestFocus()
            chatMenu?.showKeyboard(replyEditText)
        }
    }

    fun onStickerClosed() {
        chatStickerMenuButton?.setImage(IconUnify.STICKER)
        chatStickerMenuButton?.setOnClickListener {
            chatMenu?.toggleStickerMenu()
        }
    }

    fun getSendButton() : IconUnify? {
        return sendButton
    }

    fun getComposeMessageArea(): ComposeMessageAreaConstraintLayout? {
        return replyBoxLayout
    }

    fun updateStickers(
        stickers: List<StickerGroup>,
        needToUpdate: List<StickerGroup> = emptyList()
    ) {
        chatMenu?.stickerMenu?.updateStickers(stickers, needToUpdate)
    }

    fun resetTab() {
        onTabClicked(
            clickedTab = TabLayoutActiveStatus.SRW,
            additional = {
                chatMenu?.hideKeyboard()
            }
        )
    }

    fun setupListener(listener: ChatTextAreaTabLayoutListener) {
        this.listener = listener
    }

    enum class TabLayoutActiveStatus {
        SRW, ReplyBox
    }

    internal data class TabView(
        var tabLayout : View? = null,
        var tabText: Typography? = null,
        var tabBackground: View? = null,
    )

    companion object {
        private val LAYOUT = R.layout.layout_text_area_tab
    }
}

interface ChatTextAreaTabLayoutListener {
    fun onClickSRWTab()
    fun onClickReplyTab()
}

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
    private var replyEditText: EditText? = null
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
    private var tabList = hashMapOf<View?, Pair<View?, View?>>()
    private var tabState: TabLayoutActiveStatus = TabLayoutActiveStatus.SRW

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

        tabList.apply {
            this[textTabSRW] = (Pair(tabSRW, srwLayout))
            this[textTabReplyBox] = (Pair(tabReplyBox, replyBoxLayout))
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
            tabState = TabLayoutActiveStatus.SRW
            updateTab(it)
            chatMenu?.hideKeyboard()
        }
        tabReplyBox?.setOnClickListener {
            tabState = TabLayoutActiveStatus.ReplyBox
            updateTab(it)
            replyEditText?.requestFocus()
            chatMenu?.showKeyboard(replyEditText)
        }
    }

    fun initSrw(
        srwQuestionListener: SrwQuestionViewHolder.Listener,
        srwFrameLayoutListener: SrwFrameLayout.Listener
    ) {
        srwLayout?.initialize(srwQuestionListener, srwFrameLayoutListener)
        srwLayout?.setSrwTitleVisibility(false)
        srwLayout?.setContentMargin()
        srwLayout?.isSrwBubble = false
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

    private fun updateTab(pickedView: View) {
        chatMenu?.hideMenu()
        changeTabState(pickedView, isActive = true)
        showTabContent(pickedView)
        changeToInactive(pickedView)
    }

    private fun changeTabState(pickedView: View, isActive: Boolean) {
        if (isActive) {
            pickedView.background = tabBackgroundActive
        } else {
            pickedView.background = tabBackgroundInactive
        }
    }

    private fun showTabContent(pickedView: View) {
        tabList.forEach {
            val text = it.key
            val tab = it.value.first
            val layout = it.value.second
            if (tab == pickedView) {
                layout?.show()
                tab.bringToFront()
                text?.bringToFront()
                return@forEach
            }
        }
    }

    private fun changeToInactive(pickedView: View) {
        tabList.forEach {
            val tab = it.value.first
            val layout = it.value.second
            if (tab != pickedView) {
                tab?.let { tabView ->
                    changeTabState(tabView, isActive = false)
                    layout?.hide()
                }
            }
        }
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

    internal enum class TabLayoutActiveStatus {
        SRW, ReplyBox
    }

    companion object {
        private val LAYOUT = R.layout.layout_text_area_tab
    }
}
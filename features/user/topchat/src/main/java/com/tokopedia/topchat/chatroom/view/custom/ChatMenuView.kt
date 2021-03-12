package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder

class ChatMenuView : FrameLayout, AttachmentItemViewHolder.AttachmentViewHolderListener {

    var isVisible = false
    var isShowing = false
    var showDelayed = false
    var isKeyboardOpened = false

    var attachmentMenu: ChatMenuAttachmentView? = null
    var stickerMenu: ChatMenuStickerView? = null
    private var previousSelectedMenu: ViewGroup? = null
    private var selectedMenu: ViewGroup? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        bindViewId()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun bindViewId() {
        attachmentMenu = findViewById(R.id.rv_topchat_attachment_menu)
        stickerMenu = findViewById(R.id.ll_sticker_container)
    }

    override fun closeMenu() {
        hideMenu()
    }

    fun setupAttachmentMenu(attachmentMenuListener: AttachmentMenu.AttachmentMenuListener) {
        attachmentMenu?.setAttachmentMenuListener(attachmentMenuListener)
        attachmentMenu?.setAttachmentMenuViewHolderListener(this)
    }

    fun toggleAttachmentMenu() {
        selectedMenu = attachmentMenu
        toggleMenu {
            previousSelectedMenu = selectedMenu
            attachmentMenu?.show()
            stickerMenu?.hide()
        }
    }

    fun toggleStickerMenu() {
        selectedMenu = stickerMenu
        toggleMenu {
            previousSelectedMenu = selectedMenu
            stickerMenu?.show()
            attachmentMenu?.hide()
        }
    }

    private fun toggleMenu(onShow: () -> Unit) {
        if (isShowing) return
        if (isVisible && previousSelectedMenu == selectedMenu) {
            hideMenu()
        } else {
            onShow()
            showMenu()
        }
    }

    fun hideMenu() {
        if (isVisible && !isShowing) {
            isVisible = false
            attachmentMenu?.hide()
            stickerMenu?.hide()
            hide()
        }
    }

    private fun showMenu() {
        isShowing = true
        if (!isKeyboardOpened) {
            showMenuImmediately()
        } else {
            showDelayed = true
            hideKeyboard()
        }
    }

    private fun showMenuImmediately() {
        isShowing = false
        showDelayed = false
        isVisible = true
        show()
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun showKeyboard(view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun showMenuDelayed() {
        if (showDelayed) {
            showMenuImmediately()
        }
    }

    fun addVoucherAttachmentMenu() {
        attachmentMenu?.addVoucherAttachmentMenu()
    }

    fun setStickerMenuListener(stickerMenuListener: ChatMenuStickerView.StickerMenuListener?) {
        stickerMenu?.menuListener = stickerMenuListener
    }

    fun setStickerListener(listener: StickerViewHolder.Listener) {
        stickerMenu?.stickerListener = listener
    }

    companion object {
        private val LAYOUT = R.layout.partial_chat_menu_view
    }

}
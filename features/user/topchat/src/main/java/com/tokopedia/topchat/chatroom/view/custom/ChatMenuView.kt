package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R

class ChatMenuView : FrameLayout, AttachmentItemViewHolder.AttachmentViewHolderListener {

    var isVisible = false
    var isShowing = false
    var showDelayed = false
    var isKeyboardOpened = false

    private var attachmentMenu: ChatMenuAttachmentView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        bindViewId()
    }

    fun setupAttachmentMenu(attachmentMenuListener: AttachmentMenu.AttachmentMenuListener) {
        attachmentMenu?.setAttachmentMenuListener(attachmentMenuListener)
        attachmentMenu?.setAttachmentMenuViewHolderListener(this)
    }

    override fun closeMenu() {
        hide()
    }

    fun toggleAttachmentMenu() {
        if (isShowing) return
        if (isVisible) {
            hideMenu()
        } else {
            showMenu()
        }
    }

    fun hideMenu() {
        if (isVisible && !isShowing) {
            isVisible = false
            hide()
        }
    }

    private fun showMenu() {
        isShowing = true
        if (!isKeyboardOpened) {
            showMenuImmedietly()
        } else {
            showDelayed = true
            hideKeyboard()
        }
    }

    fun showMenuImmedietly() {
        isShowing = false
        showDelayed = false
        isVisible = true
        show()
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun bindViewId() {
        attachmentMenu = findViewById(R.id.rv_topchat_attachment_menu)
    }

    fun showMenuDelayed() {
        if (showDelayed) {
            showMenuImmedietly()
        }
    }

    fun addVoucherAttachmentMenu() {
        attachmentMenu?.addVoucherAttachmentMenu()
    }

    companion object {
        private val LAYOUT = R.layout.partial_chat_menu_view
    }

}
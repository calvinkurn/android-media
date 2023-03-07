package com.tokopedia.tokochat_common.view.customview.attachment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.view.listener.TokoChatAttachmentMenuListener

/**
 * Custom Tokochat Menu Layout
 * 1. Attachment menu
 * 2. Sticker menu (future update)
 */
class TokoChatMenuLayout : ConstraintLayout {

    var isVisible = false
    var isShowing = false
    var showDelayed = false
    var isKeyboardOpened = false

    var attachmentMenu: TokoChatMenuAttachmentRecyclerView? = null
    private var previousSelectedMenu: ChatMenuType? = null
    private var selectedMenu: ChatMenuType? = null
    private var visibilityListener: VisibilityListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        initViewLayout()
        bindViewId()
    }

    interface VisibilityListener {
        fun onShow()
        fun onHide()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun bindViewId() {
        attachmentMenu = findViewById(R.id.tokochat_rv_attachment_menu)
    }

    private fun updateAttachmentMenu(listener: TokoChatAttachmentMenuListener) {
        attachmentMenu?.updateAttachmentMenu(listener)
    }

    fun toggleAttachmentMenu(menuType: ChatMenuType) {
        selectedMenu = menuType
        toggleMenu {
            previousSelectedMenu = selectedMenu
            attachmentMenu?.show()
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

    private fun hideMenu() {
        if (isVisible && !isShowing) {
            isVisible = false
            attachmentMenu?.hide()
            hide()
            visibilityListener?.onHide()
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
        visibilityListener?.onShow()
    }

    fun hideKeyboard() {
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

    fun setVisibilityListener(visibilityListener: VisibilityListener) {
        this.visibilityListener = visibilityListener
    }

    enum class ChatMenuType {
        ATTACHMENT_MENU,
        STICKER_MENU //Reserved for future use
    }

    companion object {
        private val LAYOUT = R.layout.tokochat_partial_chat_menu
    }

}

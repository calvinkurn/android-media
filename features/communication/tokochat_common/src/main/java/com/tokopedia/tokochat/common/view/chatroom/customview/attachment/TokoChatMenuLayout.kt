package com.tokopedia.tokochat.common.view.chatroom.customview.attachment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showKeyboard
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatAttachmentMenuListener

/**
 * Custom Tokochat Menu Layout
 * 1. Attachment menu
 * 2. Sticker menu (future update)
 */
class TokoChatMenuLayout : ConstraintLayout {

    var attachmentMenu: TokoChatMenuAttachmentRecyclerView? = null
    private var previousSelectedMenu: ChatMenuType? = null
    private var listener: TokoChatAttachmentMenuListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        initViewLayout()
        bindViewId()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun bindViewId() {
        attachmentMenu = findViewById(R.id.tokochat_rv_attachment_menu)
    }

    fun updateAttachmentMenu(
        listener: TokoChatAttachmentMenuListener,
        showImageAttachment: Boolean
    ) {
        this.listener = listener
        attachmentMenu?.updateAttachmentMenu(
            listener = listener,
            showImageAttachment = showImageAttachment
        )
    }

    fun toggleAttachmentMenu(
        menuType: ChatMenuType,
        view: View?
    ) {
        // If selected menu clicked again, hide the menu view
        previousSelectedMenu = if (previousSelectedMenu == menuType) {
            view?.showKeyboard()
            hideMenu()

            // Reset previous selected menu
            null
        } else {
            view?.hideKeyboard() // Hide keyboard
            showMenu() // Show Menu

            // Save to previous selected menu
            menuType
        }
    }

    private fun hideMenu() {
        attachmentMenu?.hide()
        hide()
    }

    private fun showMenu() {
        attachmentMenu?.show()
        show()
    }

    enum class ChatMenuType {
        ATTACHMENT_MENU,
        STICKER_MENU // Reserved for future use
    }

    companion object {
        private val LAYOUT = R.layout.tokochat_partial_chat_menu
    }
}

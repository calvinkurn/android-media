package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder
import timber.log.Timber

class ChatMenuView : FrameLayout, AttachmentItemViewHolder.AttachmentViewHolderListener {

    var isVisible = false
    var isShowing = false
    var showDelayed = false
    var isKeyboardOpened = false

    var attachmentMenu: ChatMenuAttachmentView? = null
    var stickerMenu: ChatMenuStickerView? = null
    private var previousSelectedMenu: ViewGroup? = null
    private var selectedMenu: ViewGroup? = null
    private var visibilityListener: VisibilityListener? = null

    private val slideUp = AnimationUtils.loadAnimation(context, R.anim.topchat_reply_area_pull_up)
    private val slideDown = AnimationUtils.loadAnimation(context, R.anim.topchat_reply_area_pull_down)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        bindViewId()
        initAnimationListener()
    }

    interface VisibilityListener {
        fun onShow()
        fun onHide()
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

    fun toggleAttachmentMenu(additionalAction: (Boolean) -> Unit) {
        selectedMenu = attachmentMenu
        toggleMenu(onShow = {
            previousSelectedMenu = selectedMenu
            attachmentMenu?.show()
            stickerMenu?.hide()
            additionalAction(true)
        }, onHide = {
            additionalAction(false)
        })
    }

    fun toggleStickerMenu(additionalAction: (Boolean) -> Unit) {
        selectedMenu = stickerMenu
        toggleMenu(onShow = {
            previousSelectedMenu = selectedMenu
            stickerMenu?.show()
            attachmentMenu?.hide()
            additionalAction(false)
        }, onHide = {
            additionalAction(true)
        })
    }

    private fun toggleMenu(onShow: () -> Unit, onHide: () -> Unit) {
        if (isShowing) return
        if (isVisible && previousSelectedMenu == selectedMenu) {
            onHide()
            hideMenu()
        } else {
            onShow()
            showMenu()
        }
    }

    fun hideMenu() {
        if (isVisible && !isShowing) {
            isVisible = false
            animateSlideDown()
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
        animateSlideUp()
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

    fun addVoucherAttachmentMenu() {
        attachmentMenu?.addVoucherAttachmentMenu()
    }

    fun setStickerMenuListener(stickerMenuListener: ChatMenuStickerView.StickerMenuListener?) {
        stickerMenu?.menuListener = stickerMenuListener
    }

    fun setStickerListener(listener: StickerViewHolder.Listener) {
        stickerMenu?.stickerListener = listener
    }

    fun setVisibilityListener(visibilityListener: VisibilityListener) {
        this.visibilityListener = visibilityListener
    }

    private fun initAnimationListener() {
        slideDown.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                //no op
            }
            override fun onAnimationEnd(p0: Animation?) {
                attachmentMenu?.hide()
                stickerMenu?.hide()
                this@ChatMenuView.hide()
            }
            override fun onAnimationRepeat(p0: Animation?) {
                //no op
            }
        })
    }

    private fun animateSlideUp() {
        try {
            (this.parent as ConstraintLayout).run {
                this@ChatMenuView.show()
                startAnimation(slideUp)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun animateSlideDown() {
        try {
            (this.parent as ConstraintLayout).run {
                startAnimation(slideDown)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    companion object {
        private val LAYOUT = R.layout.partial_chat_menu_view
    }

}

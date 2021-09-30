package com.tokopedia.topchat.chatroom.view.custom.message

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.MessageBubbleLayout.Companion.DEFAULT_MSG_ORIENTATION
import com.tokopedia.topchat.chatroom.view.custom.MessageBubbleLayout.Companion.LEFT_MSG_ORIENTATION
import com.tokopedia.topchat.chatroom.view.custom.MessageBubbleLayout.Companion.RIGHT_MSG_ORIENTATION

class ReplyBubbleAreaMessage : ConstraintLayout {

    private var title: TextView? = null
    private var desc: TextView? = null
    private var closeBtn: ImageView? = null
    private var orientation: Int = DEFAULT_MSG_ORIENTATION

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initLayout()
        initViewBinding()
    }

    var referredMsg: BaseChatViewModel? = null
        private set
    var referredParentReply: ParentReply? = null
        private set

    private fun initLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        title = findViewById(R.id.tp_reply_from)
        desc = findViewById(R.id.tp_reply_msg)
        closeBtn = findViewById(R.id.iv_rb_close)
    }

    fun bindReplyData(
        uiModel: BaseChatViewModel
    ) {
        val parentReply = uiModel.parentReply
        if (parentReply != null) {
            referTo(parentReply)
            setTitle(parentReply.mainText)
            setReplyMsg(parentReply.subText)
            updateCloseButtonState(false)
            show()
        } else {
            hide()
        }
    }

    fun composeReplyData(
        uiModel: BaseChatViewModel,
        enableCloseButton: Boolean = false
    ) {
        referTo(uiModel)
        setTitle(uiModel.from)
        setReplyMsg(uiModel.message)
        updateCloseButtonState(enableCloseButton)
        show()
    }

    fun clearReferredComposedMsg() {
        referTo(null)
        hide()
    }

    fun alignRight() {
        align(RIGHT_MSG_ORIENTATION)
    }

    fun alignLeft() {
        align(LEFT_MSG_ORIENTATION)
    }

    private fun updateCloseButtonState(enableCloseButton: Boolean) {
        if (enableCloseButton) {
            closeBtn?.show()
            closeBtn?.setOnClickListener {
                hide()
            }
        } else {
            closeBtn?.hide()
            clearReferTo()
        }
    }

    private fun clearReferTo() {
        referTo(null)
    }

    private fun referTo(uiModel: BaseChatViewModel?) {
        referredMsg = uiModel
    }

    private fun referTo(parentReply: ParentReply) {
        referredParentReply = parentReply
    }

    private fun setTitle(title: String) {
        this.title?.text = title
    }

    private fun setReplyMsg(msg: String) {
        desc?.text = msg
    }

    fun updateMessageOrientation(msgOrientation: Int) {
        this.orientation = msgOrientation
        updateBackground()
    }

    private fun updateBackground() {
        val drawableRes = when (orientation) {
            LEFT_MSG_ORIENTATION -> R.drawable.bg_chat_reply_preview_left_bubble
            RIGHT_MSG_ORIENTATION -> R.drawable.bg_chat_reply_preview_right_bubble
            else -> null
        } ?: return
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        background = drawable
    }

    private fun align(orientation: Int) {
        this.orientation = orientation
        updateBackground()
    }

    companion object {
        val LAYOUT = R.layout.partial_text_reply_bubble
    }

}
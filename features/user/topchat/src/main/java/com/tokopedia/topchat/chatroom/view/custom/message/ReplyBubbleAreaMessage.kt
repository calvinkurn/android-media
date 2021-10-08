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
import com.tokopedia.unifycomponents.Toaster

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

    var referredMsg: ParentReply? = null
        private set
    var listener: Listener? = null
        private set

    interface Listener {
        fun getUserName(senderId: String): String
        fun goToBubble(localId: String, replyTimeMillis: String)
    }

    private fun initLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        title = findViewById(R.id.tp_reply_from)
        desc = findViewById(R.id.tp_reply_msg)
        closeBtn = findViewById(R.id.iv_rb_close)
    }

    fun setReplyListener(listener: Listener) {
        this.listener = listener
    }

    fun bindReplyData(
        uiModel: BaseChatViewModel
    ) {
        val parentReply = uiModel.parentReply
        if (parentReply != null) {
            bindParentReply(parentReply)
            updateCloseButtonState(false)
            show()
        } else {
            hide()
        }
    }

    private fun bindParentReply(parentReply: ParentReply) {
        referTo(parentReply)
        setTitle(parentReply.senderId)
        setReplyMsg(parentReply.mainText)
        bindClick(parentReply)
    }

    private fun bindClick(parentReply: ParentReply) {
        setOnClickListener {
            if (!parentReply.isExpired) {
                listener?.goToBubble(
                    localId = parentReply.localId,
                    replyTimeMillis = parentReply.replyTimeMillisOffset
                )
            } else {
                val msg = context.getString(R.string.title_topchat_reply_bubble_expired)
                Toaster.build(
                    view = this,
                    text = msg,
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_ERROR,
                    actionText = "OK"
                ).show()
            }
        }
    }

    fun composeReplyData(
        referredMsg: BaseChatViewModel,
        enableCloseButton: Boolean = false
    ) {
        val parentReply = ParentReply(
            attachmentId = referredMsg.attachmentId,
            attachmentType = referredMsg.attachmentType,
            senderId = referredMsg.fromUid ?: "",
            replyTime = referredMsg.replyTime ?: "",
            mainText = referredMsg.message,
            subText = "",
            imageUrl = referredMsg.getReferredImageUrl(),
            localId = referredMsg.localId,
            source = "chat"
        )
        bindParentReply(parentReply)
        updateCloseButtonState(enableCloseButton)
        show()
    }

    fun clearReferredComposedMsg() {
        clearReferTo()
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
                clearReferTo()
                hide()
            }
        } else {
            closeBtn?.hide()
        }
    }

    private fun clearReferTo() {
        referredMsg = null
    }

    private fun referTo(parentReply: ParentReply) {
        referredMsg = parentReply
    }

    private fun setTitle(senderId: String?) {
        senderId ?: return
        val senderName = listener?.getUserName(senderId)
        this.title?.text = senderName
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
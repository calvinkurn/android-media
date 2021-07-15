package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.topchat.R

class MessageBubbleConstraintLayout : ConstraintLayout {

    private var fxChat: FlexBoxChatLayout? = null
    private var replyBubbleContainer: ConstraintLayout? = null
    private var showCheckMark = FlexBoxChatLayout.DEFAULT_SHOW_CHECK_MARK
    private var msgOrientation = DEFAULT_MSG_ORIENTATION

    constructor(context: Context?) : super(context) {
        initConfig(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initConfig(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initConfig(context, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initConfig(context, attrs)
    }

    init {
        initViewLayout()
    }

    private fun initConfig(context: Context?, attrs: AttributeSet?) {
        initViewBinding()
        initAttrs(context, attrs)
        initFlexboxChatLayout()
        initReplyBubbleBackground()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.MessageBubbleConstraintLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark = getBoolean(
                    R.styleable.MessageBubbleConstraintLayout_showCheckMark,
                    FlexBoxChatLayout.DEFAULT_SHOW_CHECK_MARK
                )
                msgOrientation = getInteger(
                    R.styleable.MessageBubbleConstraintLayout_messageOrientation,
                    DEFAULT_MSG_ORIENTATION
                )
            } finally {
                recycle()
            }
        }
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        fxChat = findViewById(R.id.fxChat)
        replyBubbleContainer = findViewById(R.id.cl_reply_container)
    }

    private fun initFlexboxChatLayout() {
        fxChat?.setShowCheckMark(showCheckMark)
    }

    private fun initReplyBubbleBackground() {
        val drawableRes = when (msgOrientation) {
            LEFT_MSG_ORIENTATION -> R.drawable.bg_chat_reply_preview_left_bubble
            RIGHT_MSG_ORIENTATION -> R.drawable.bg_chat_reply_preview_right_bubble
            else -> null
        } ?: return
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        replyBubbleContainer?.background = drawable
    }

    companion object {
        val LAYOUT = R.layout.partial_chat_messsage_bubble

        const val LEFT_MSG_ORIENTATION = 0
        const val RIGHT_MSG_ORIENTATION = 1
        const val DEFAULT_MSG_ORIENTATION = LEFT_MSG_ORIENTATION
    }
}
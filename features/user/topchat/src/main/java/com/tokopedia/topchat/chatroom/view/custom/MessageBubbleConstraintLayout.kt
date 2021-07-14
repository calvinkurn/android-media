package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.topchat.R

class MessageBubbleConstraintLayout : ConstraintLayout {

    private var fxChat: FlexBoxChatLayout? = null
    private var showCheckMark = FlexBoxChatLayout.DEFAULT_SHOW_CHECK_MARK

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
        initViewBinding()
    }

    private fun initConfig(context: Context?, attrs: AttributeSet?) {
        initAttrs(context, attrs)
        initFlexboxChatLayout()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.FlexBoxChatLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark = getBoolean(
                    R.styleable.MessageBubbleConstraintLayout_showCheckMark,
                    FlexBoxChatLayout.DEFAULT_SHOW_CHECK_MARK
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
    }


    private fun initFlexboxChatLayout() {
        fxChat?.setShowCheckMark(showCheckMark)
    }

    companion object {
        val LAYOUT = R.layout.partial_chat_messsage_bubble
    }
}
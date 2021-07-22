package com.tokopedia.topchat.chatroom.view.custom.message

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.topchat.R
import com.tokopedia.unifyprinciples.Typography

class TextReplyBubbleAreaMessage : ReplyBubbleAreaMessage {

    private var title: Typography? = null
    private var desc: Typography? = null

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

    private fun initLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        title = findViewById(R.id.tp_reply_from)
        desc = findViewById(R.id.tp_reply_msg)
    }

    override fun bindMessageReplyData(messageUiModel: MessageViewModel) {
        setTitle(messageUiModel.from)
        setReplyMsg(messageUiModel.message)
    }

    private fun setTitle(title: String) {
        this.title?.text = title
    }

    private fun setReplyMsg(msg: String) {
        desc?.text = msg
    }

    companion object {
        val LAYOUT = R.layout.partial_text_reply_bubble
    }
}
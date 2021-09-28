package com.tokopedia.topchat.chatroom.view.custom.message

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R

class ReplyBubbleAreaMessage : ConstraintLayout {

    private var title: TextView? = null
    private var desc: TextView? = null
    private var closeBtn: ImageView? = null

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

    companion object {
        val LAYOUT = R.layout.partial_text_reply_bubble
    }

}
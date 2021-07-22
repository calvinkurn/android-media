package com.tokopedia.topchat.chatroom.view.custom.message

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.MessageViewModel

abstract class ReplyBubbleAreaMessage : ConstraintLayout {

    constructor(context: Context?) : super(context) {}
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

    fun bindReplyData(uiModel: Visitable<*>) {
        when (uiModel) {
            is MessageViewModel -> bindMessageReplyData(uiModel)
            else -> throw IllegalStateException("Unsupported UiModel reply bubble")
        }
    }

    protected open fun bindMessageReplyData(messageUiModel: MessageViewModel) {}

}
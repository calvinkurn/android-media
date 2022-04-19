package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.talk.databinding.WidgetTalkReplyUnmaskCardBinding
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyUnmaskCardListener
import com.tokopedia.unifycomponents.BaseCustomView


class TalkReplyUnmaskCard : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = WidgetTalkReplyUnmaskCardBinding.inflate(LayoutInflater.from(context), this, true)

    fun setListener(talkReplyUnmaskCardListener: TalkReplyUnmaskCardListener, commentId: String) {
        binding.talkReplyUnmaskNoOption.setOnClickListener {
            talkReplyUnmaskCardListener.onUnmaskQuestionOptionSelected(false, commentId)
        }
        binding.talkReplyUnmaskYesOption.setOnClickListener {
            talkReplyUnmaskCardListener.onUnmaskQuestionOptionSelected(true, commentId)
        }
    }
}
package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyUnmaskCardListener
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_reply_unmask_card.view.*


class TalkReplyUnmaskCard : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_talk_reply_unmask_card, this)
    }

    fun setListener(talkReplyUnmaskCardListener: TalkReplyUnmaskCardListener, commentId: String) {
        this.talkReplyUnmaskNoOption.setOnClickListener { 
            talkReplyUnmaskCardListener.onUnmaskQuestionOptionSelected(false, commentId)
        }
        this.talkReplyUnmaskYesOption.setOnClickListener {
            talkReplyUnmaskCardListener.onUnmaskQuestionOptionSelected(true, commentId)
        }
    }
}
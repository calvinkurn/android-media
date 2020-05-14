package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.graphics.Rect
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.talk.feature.reply.presentation.util.textwatcher.TalkReplyTextWatcher
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTextboxListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_reply_textbox.view.*

class TalkReplyTextbox : BaseCustomView {

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
        View.inflate(context, R.layout.widget_talk_reply_textbox, this)
    }

    fun bind(profilePicture: String, talkReplyTextboxListener: TalkReplyTextboxListener, textLimit: Int) {
        replyUserProfilePicture.loadImageCircle(profilePicture)
        replyAttachProduct.setOnClickListener {
            talkReplyTextboxListener.onAttachProductButtonClicked()
        }
        replySendButton.setOnClickListener {
            if(replyEditText.text.toString().length <= textLimit) {
                talkReplyTextboxListener.onSendButtonClicked(replyEditText.text.toString())
            }
        }
        val textWatcher = TalkReplyTextWatcher(textLimit, talkReplyTextboxListener, replyEditText)
        replyEditText.addTextChangedListener(textWatcher)
        replyEditText.filters = arrayOf(InputFilter.LengthFilter(textLimit))
    }

    fun reset() {
        replyEditText.setText("")
        replyEditText.clearFocus()
    }
}
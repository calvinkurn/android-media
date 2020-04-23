package com.tokopedia.talk.feature.reply.presentation.util.textwatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTextboxListener

class TalkReplyTextWatcher(private val textLimit: Int, private val talkReplyTextboxListener: TalkReplyTextboxListener) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        //No Op
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //No Op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            if(it.length >= textLimit ) {
                talkReplyTextboxListener.onMaximumTextLimitReached()
            }
        }
    }

}
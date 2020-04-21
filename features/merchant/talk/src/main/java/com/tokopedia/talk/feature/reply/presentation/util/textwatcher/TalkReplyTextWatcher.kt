package com.tokopedia.talk.feature.reply.presentation.util.textwatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnMaximumLimitReachedListener

class TalkReplyTextWatcher(private val replyTextBox: EditText, onMaximumLimitReachedListener: OnMaximumLimitReachedListener) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //No Op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

}
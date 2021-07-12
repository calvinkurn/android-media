package com.tokopedia.topchat.chatroom.view.custom

import android.text.Editable
import android.text.TextWatcher
import com.tokopedia.topchat.chatroom.view.listener.ReplyBoxTextListener

class SendButtonTextWatcher (
        private val replyBoxTextListener: ReplyBoxTextListener
): TextWatcher {

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s.isNullOrEmpty()) {
            replyBoxTextListener.onReplyBoxEmpty()
        } else {
            replyBoxTextListener.onReplyBoxNotEmpty()
        }
    }
}
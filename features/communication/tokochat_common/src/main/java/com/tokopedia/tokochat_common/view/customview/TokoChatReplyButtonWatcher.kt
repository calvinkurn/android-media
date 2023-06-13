package com.tokopedia.tokochat_common.view.customview

import android.text.Editable
import android.text.TextWatcher
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener

class TokoChatReplyButtonWatcher(
    private val replyTextListener: TokoChatReplyTextListener,
    private val listener: Listener?
) : TextWatcher {

    interface Listener {
        fun onComposeTextLimitExceeded(offset: Int)
        fun hideTextLimitError()
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isEligibleToSendMsg(s)) {
            replyTextListener.enableSendButton()
            listener?.hideTextLimitError()
        } else {
            if (isExceedLimit(s)) {
                val offset = calculateOffset(s)
                listener?.onComposeTextLimitExceeded(offset)
            } else {
                listener?.hideTextLimitError()
            }
            replyTextListener.disableSendButton(isExceedLimit(s))
        }
    }

    private fun calculateOffset(s: CharSequence?): Int {
        s ?: return 0
        return s.length - MAX_CHAR
    }

    private fun isEligibleToSendMsg(s: CharSequence?): Boolean {
        return s != null && s.isNotBlank() && !isExceedLimit(s)
    }

    private fun isExceedLimit(s: CharSequence?): Boolean {
        return s != null && s.length > MAX_CHAR
    }

    companion object {
        const val MAX_CHAR = 1000
    }
}

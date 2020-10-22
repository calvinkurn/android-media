package com.tokopedia.chatbot

import android.widget.TextView

object EllipsizeMaker {

    const val MESSAGE_LINE_COUNT = 10

    fun getTruncatedMsg(message: TextView): CharSequence? {
        return if (message.layout != null) {
            val lineEndIndex = message.layout.getLineEnd(MESSAGE_LINE_COUNT - 1)
            String.format("%s%s", message.text.subSequence(0, lineEndIndex - 3), message.context.getString(R.string.cb_bot_three_dots))
        } else {
            message.text
        }
    }
}
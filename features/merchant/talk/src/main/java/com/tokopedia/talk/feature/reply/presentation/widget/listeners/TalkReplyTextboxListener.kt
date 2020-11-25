package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface TalkReplyTextboxListener {
    fun onAttachProductButtonClicked()
    fun onMaximumTextLimitReached()
    fun onSendButtonClicked(text: String)
}
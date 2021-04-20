package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface TalkReplyUnmaskCardListener {
    fun onUnmaskQuestionOptionSelected(isMarkNotFraud: Boolean, commentId: String)
}
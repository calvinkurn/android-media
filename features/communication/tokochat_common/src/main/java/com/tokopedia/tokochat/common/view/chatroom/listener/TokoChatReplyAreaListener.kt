package com.tokopedia.tokochat.common.view.chatroom.listener

interface TokoChatReplyAreaListener {
    fun onClickAttachmentButton()
    fun shouldShowAttachmentButton(): Boolean
    fun trackClickComposeArea()
}

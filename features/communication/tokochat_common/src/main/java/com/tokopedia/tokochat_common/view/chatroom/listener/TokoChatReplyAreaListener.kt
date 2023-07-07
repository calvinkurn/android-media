package com.tokopedia.tokochat_common.view.chatroom.listener

interface TokoChatReplyAreaListener {
    fun onClickAttachmentButton()
    fun shouldShowAttachmentButton(): Boolean
    fun trackClickComposeArea()
}

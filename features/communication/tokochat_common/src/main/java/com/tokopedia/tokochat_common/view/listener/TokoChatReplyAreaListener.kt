package com.tokopedia.tokochat_common.view.listener

interface TokoChatReplyAreaListener {
    fun onClickAttachmentButton()
    fun shouldShowAttachmentButton(): Boolean
    fun trackClickComposeArea()
}

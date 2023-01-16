package com.tokopedia.tokochat_common.view.listener

interface TokoChatReplyTextListener {
    fun disableSendButton(isExceedLimit: Boolean = false)
    fun enableSendButton()
}

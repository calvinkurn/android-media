package com.tokopedia.tokochat_common.view.chatroom.listener

interface TokoChatReplyTextListener {
    fun disableSendButton(isExceedLimit: Boolean = false)
    fun enableSendButton()
    fun onReplyAreaFocusChanged(isFocused: Boolean)
}

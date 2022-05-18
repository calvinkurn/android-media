package com.tokopedia.topchat.chatroom.view.listener

interface ReplyBoxTextListener {
    fun disableSendButton(isExceedLimit: Boolean = false)
    fun enableSendButton()
}
package com.tokopedia.topchat.chatroom.view.listener

interface SendButtonListener {
    fun onSendClicked(message: String, generateStartTime: String)
}

package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class BigReplyBoxState {
    data class BigReplyBoxContents(val title: String, val placeHolder: String) : BigReplyBoxState()
}

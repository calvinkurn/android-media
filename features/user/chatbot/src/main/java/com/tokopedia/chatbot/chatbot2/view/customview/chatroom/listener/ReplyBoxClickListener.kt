package com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener

interface ReplyBoxClickListener {
    fun onAttachmentMenuClicked()
    fun goToBigReplyBoxBottomSheet()
    fun getMessageContentFromBottomSheet(message: String)
}

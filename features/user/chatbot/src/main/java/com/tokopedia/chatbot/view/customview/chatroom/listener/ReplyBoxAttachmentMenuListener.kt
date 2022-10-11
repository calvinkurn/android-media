package com.tokopedia.chatbot.view.customview.chatroom.listener

interface ReplyBoxAttachmentMenuListener {
    fun onAttachmentMenuClicked()
    fun goToBigReplyBoxBottomSheet()
    fun getMessageContentFromBottomSheet(s: String)
}

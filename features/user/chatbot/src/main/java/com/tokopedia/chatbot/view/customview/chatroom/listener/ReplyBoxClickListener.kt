package com.tokopedia.chatbot.view.customview.chatroom.listener

interface ReplyBoxClickListener {
    fun onAttachmentMenuClicked()
    fun goToBigReplyBoxBottomSheet()
    fun getMessageContentFromBottomSheet(s: String)
}

package com.tokopedia.chatbot.view.customview.chatroom.listener

interface ReplyBoxClickListener {
    fun onAttachmentMenuClicked()
    fun goToBigReplyBoxBottomSheet(isError: Boolean)
    fun getMessageContentFromBottomSheet(s: String)
}

package com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener

interface ReplyBoxClickListener {
    fun onAttachmentMenuClicked()
    fun goToBigReplyBoxBottomSheet(isError: Boolean)
    fun getMessageContentFromBottomSheet(message: String)
    fun dismissBigReplyBoxBottomSheet(message: String, wordLength: Int)
}

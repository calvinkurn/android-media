package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.ChatReplies

sealed class ChatDataState {
    data class SuccessChatDataState(val chatroomViewModel: ChatroomViewModel, val chatReplies: ChatReplies) : ChatDataState()
    data class HandleFailureChatData(val throwable: Throwable) : ChatDataState()
}

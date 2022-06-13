package com.tokopedia.topchat.chatroom.domain.pojo

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.ChatReplies

data class GetChatResult (
    var chatroomViewModel: ChatroomViewModel,
    var chatReplies: ChatReplies
)
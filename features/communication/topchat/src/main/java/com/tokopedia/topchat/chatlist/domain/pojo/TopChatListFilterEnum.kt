package com.tokopedia.topchat.chatlist.domain.pojo

import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant

enum class TopChatListFilterEnum(val filterString: String) {
    FILTER_ALL(ChatListQueriesConstant.PARAM_FILTER_ALL),
    FILTER_UNREAD(ChatListQueriesConstant.PARAM_FILTER_UNREAD),
    FILTER_UNREPLIED(ChatListQueriesConstant.PARAM_FILTER_UNREPLIED),
    FILTER_BROADCAST(ChatListQueriesConstant.PARAM_FILTER_BROADCAST),
    FILTER_TOPBOT(ChatListQueriesConstant.PARAM_FILTER_TOPBOT)
}

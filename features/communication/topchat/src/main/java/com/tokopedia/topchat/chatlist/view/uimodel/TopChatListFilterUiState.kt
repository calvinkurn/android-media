package com.tokopedia.topchat.chatlist.view.uimodel

import com.tokopedia.topchat.chatlist.domain.pojo.TopChatListFilterEnum

data class TopChatListFilterUiState (
    val selectedFilter: TopChatListFilterEnum = TopChatListFilterEnum.FILTER_ALL,
    val filterListSeller: List<Pair<TopChatListFilterEnum, String>> = listOf(),
    val filterListBuyer: List<Pair<TopChatListFilterEnum, String>> = listOf()
)

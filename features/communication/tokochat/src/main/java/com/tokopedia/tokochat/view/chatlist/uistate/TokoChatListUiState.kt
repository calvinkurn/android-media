package com.tokopedia.tokochat.view.chatlist.uistate

import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel

data class TokoChatListUiState(
    val chatItemList: List<TokoChatListItemUiModel> = listOf(),
    val isLoading: Boolean = false,
    val page: Int = 0,
    val hasNextPage: Boolean = true,
    val errorMessage: String? = null
)

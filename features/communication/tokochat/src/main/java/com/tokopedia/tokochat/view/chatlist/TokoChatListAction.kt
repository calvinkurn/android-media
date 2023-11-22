package com.tokopedia.tokochat.view.chatlist

sealed interface TokoChatListAction {
    object RefreshPage : TokoChatListAction
    object LoadNextPage : TokoChatListAction
    data class NavigateToPage(val applink: String) : TokoChatListAction
}

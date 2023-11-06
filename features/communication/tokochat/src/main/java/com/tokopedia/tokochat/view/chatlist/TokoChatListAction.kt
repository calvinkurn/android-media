package com.tokopedia.tokochat.view.chatlist

import android.content.Intent

sealed class TokoChatListAction {
    object RefreshPage : TokoChatListAction()
    object LoadNextPage : TokoChatListAction()
    data class NavigateWithIntent(val intent: Intent) : TokoChatListAction()
    data class NavigateToPage(val applink: String) : TokoChatListAction()
}

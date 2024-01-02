package com.tokopedia.shareexperience.ui

sealed interface ShareExAction {
    object FetchShareData : ShareExAction
    object InitializePage : ShareExAction
    data class UpdateShareBody(val position: Int) : ShareExAction
    data class UpdateShareImage(val imageUrl: String) : ShareExAction
    data class NavigateToPage(val appLink: String) : ShareExAction
}

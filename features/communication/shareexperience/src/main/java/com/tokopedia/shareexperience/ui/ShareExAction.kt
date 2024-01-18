package com.tokopedia.shareexperience.ui

import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum

sealed interface ShareExAction {
    data class FetchShareData(
        val id: String,
        val source: ShareExPageTypeEnum,
        val defaultUrl: String,
        val defaultImageUrl: String,
        val selectedIdChip: String
    ) : ShareExAction
    object InitializePage : ShareExAction
    data class UpdateShareBody(val position: Int) : ShareExAction
    data class UpdateShareImage(val imageUrl: String) : ShareExAction
    data class NavigateToPage(val appLink: String) : ShareExAction
}

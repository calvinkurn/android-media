package com.tokopedia.shareexperience.ui

import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum

sealed interface ShareExAction {
    data class FetchShareData(
        val id: String,
        val source: ShareExPageTypeEnum,
        val defaultUrl: String,
        val selectedIdChip: String
    ) : ShareExAction
    object InitializePage : ShareExAction
    data class UpdateShareBody(val position: Int, val text: String) : ShareExAction
    data class UpdateShareImage(val imageUrl: String) : ShareExAction
    data class GenerateLink(val channelEnum: ShareExChannelEnum) : ShareExAction
    data class NavigateToPage(val appLink: String) : ShareExAction
}

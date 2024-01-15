package com.tokopedia.shareexperience.ui

import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum

sealed interface ShareExAction {
    data class FetchShareData(
        val id: String,
        val source: ShareExPageTypeEnum,
        val defaultUrl: String,
        val selectedIdChip: String
    ) : ShareExAction
    object InitializePage : ShareExAction
    data class UpdateShareBody(val position: Int) : ShareExAction
    data class UpdateShareImage(val imageUrl: String) : ShareExAction
    data class GenerateLink(val channelEnum: ShareExChannelEnum) : ShareExAction
    data class NavigateToPage(val appLink: String) : ShareExAction
}

package com.tokopedia.shareexperience.ui

import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel

sealed interface ShareExAction {
    object InitializePage : ShareExAction
    data class UpdateShareBody(val position: Int, val text: String) : ShareExAction
    data class UpdateShareImage(val imageUrl: String) : ShareExAction
    data class GenerateLink(val channelItemModel: ShareExChannelItemModel) : ShareExAction
}

package com.tokopedia.shareexperience.domain.model.channel

data class ShareExChannelItemModel(
    val idEnum: ShareExChannelEnum = ShareExChannelEnum.OTHER,
    val title: String = "",
    val icon: Int = 0
)

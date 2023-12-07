package com.tokopedia.shareexperience.domain.model.channel

data class ShareExChannelModel(
    val description: String = "",
    val listChannel: List<ShareExChannelItemModel> = listOf()
)

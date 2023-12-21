package com.tokopedia.shareexperience.domain.model.property

import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel

data class ShareExBodyModel(
    val subtitle: String = "",
    val listChip: List<String> = listOf(),
    val listShareProperty: List<ShareExPropertyModel> = listOf(),
    val socialChannel: ShareExChannelModel = ShareExChannelModel(),
    val commonChannel: ShareExChannelModel = ShareExChannelModel()
)

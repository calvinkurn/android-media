package com.tokopedia.shareexperience.domain.model.property

import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel

data class ShareExBodyModel(
    val title: String = "",
    val listImage: List<String> = listOf(),
    val listChip: List<ShareExChipModel> = listOf(),
    val channel: ShareExChannelModel = ShareExChannelModel()
)

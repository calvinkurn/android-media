package com.tokopedia.shareexperience.domain.model.channel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExChannelModel(
    val description: String = "",
    val listChannel: List<ShareExChannelItemModel> = listOf()
): Parcelable

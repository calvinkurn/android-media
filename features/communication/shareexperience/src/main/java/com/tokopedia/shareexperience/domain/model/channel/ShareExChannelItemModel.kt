package com.tokopedia.shareexperience.domain.model.channel

import android.content.Intent
import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExChannelItemModel(
    val channelEnum: ShareExChannelEnum = ShareExChannelEnum.OTHERS,
    val title: String = "",
    val icon: Int = 0,
    val mimeType: ShareExMimeTypeEnum,
    val packageName: String,
    val actionIntent: String = Intent.ACTION_SEND,
    val platform: String = "",
    val imageResolution: String = ""
) : Parcelable

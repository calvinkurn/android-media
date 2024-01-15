package com.tokopedia.shareexperience.domain.model.channel

import android.content.Intent
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum

data class ShareExChannelItemModel(
    val idEnum: ShareExChannelEnum = ShareExChannelEnum.OTHERS,
    val title: String = "",
    val icon: Int = 0,
    val mimeType: ShareExMimeTypeEnum,
    val packageName: String = "",
    val actionIntent: String = Intent.ACTION_SEND
)

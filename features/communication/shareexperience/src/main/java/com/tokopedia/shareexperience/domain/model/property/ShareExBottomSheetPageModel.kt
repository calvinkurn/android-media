package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExBottomSheetPageModel(
    val listChip: List<String> = listOf(),
    val listShareProperty: List<ShareExPropertyModel> = listOf(),
    val socialChannel: ShareExChannelModel = ShareExChannelModel(),
    val commonChannel: ShareExChannelModel = ShareExChannelModel()
) : Parcelable

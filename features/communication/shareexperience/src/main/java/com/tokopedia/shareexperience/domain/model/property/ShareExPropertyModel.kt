package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExPropertyModel(
    val shareId: String? = null,
    val listImage: List<String> = listOf(),
    val title: String = "",
    val affiliate: ShareExAffiliateModel = ShareExAffiliateModel(),
    val linkProperties: ShareExLinkProperties = ShareExLinkProperties(),
    val imageGenerator: ShareExImageGeneratorPropertyModel? = null,
    val socialChannel: ShareExChannelModel = ShareExChannelModel(),
    val commonChannel: ShareExChannelModel = ShareExChannelModel()
) : Parcelable

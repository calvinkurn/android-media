package com.tokopedia.shareexperience.data.dto.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shareexperience.domain.model.ShareExRequest

data class ShareExDiscoveryRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("campaignId")
    val id: Long
) : ShareExRequest

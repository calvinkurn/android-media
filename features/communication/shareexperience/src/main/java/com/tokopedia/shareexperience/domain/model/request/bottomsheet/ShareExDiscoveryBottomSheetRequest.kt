package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExDiscoveryBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("campaignId")
    val id: Long
) : ShareExBottomSheetRequest

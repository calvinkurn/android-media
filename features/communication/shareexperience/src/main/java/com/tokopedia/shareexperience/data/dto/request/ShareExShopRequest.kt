package com.tokopedia.shareexperience.data.dto.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shareexperience.domain.model.ShareExRequest

data class ShareExShopRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("shopId")
    val id: Long
) : ShareExRequest

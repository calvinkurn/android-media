package com.tokopedia.shareexperience.data.dto.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shareexperience.domain.model.ShareExRequest

data class ShareExProductRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("productId")
    val id: Long
) : ShareExRequest

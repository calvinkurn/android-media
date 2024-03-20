package com.tokopedia.shareexperience.data.dto.affiliate.generatelink

import com.google.gson.annotations.SerializedName

data class ShareExGenerateAffiliateLinkUrlResponseDto(
    @SerializedName("ShortURL")
    val shortUrl: String = "",

    @SerializedName("RegularURL")
    val regularUrl: String = ""
)

package com.tokopedia.shareexperience.data.dto.affiliate.generatelink

import com.google.gson.annotations.SerializedName

data class ShareExGenerateAffiliateLinkDataResponseDto(
    @SerializedName("Status")
    val status: Int = 0,

    @SerializedName("Type")
    val type: String = "",

    @SerializedName("Error")
    val error: String = "",

    @SerializedName("Identifier")
    val identifier: String = "",

    @SerializedName("IdentifierType")
    val identifierType: Int = 0,

    @SerializedName("LinkID")
    val linkID: Long = 0,

    @SerializedName("URL")
    val url: ShareExGenerateAffiliateLinkUrlResponseDto = ShareExGenerateAffiliateLinkUrlResponseDto()
)

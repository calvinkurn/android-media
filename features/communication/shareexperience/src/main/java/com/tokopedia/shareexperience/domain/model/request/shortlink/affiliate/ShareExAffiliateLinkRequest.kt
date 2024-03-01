package com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkRequest(
    @SerializedName("Type")
    val type: String = "",

    @SerializedName("URL")
    val url: String = "",

    @SerializedName("Identifier")
    val identifier: String = "",

    @SerializedName("IdentifierType")
    val identifierType: Int = 0,

    @SerializedName("AdditionalParams")
    val additionalParams: List<ShareExAffiliateLinkAdditionalParamRequest> = listOf()
)

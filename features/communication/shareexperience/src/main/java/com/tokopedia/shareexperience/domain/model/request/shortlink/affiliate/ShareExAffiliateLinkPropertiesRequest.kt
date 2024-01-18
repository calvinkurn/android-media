package com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkPropertiesRequest(
    @SerializedName("source")
    val source: String = "",

    @SerializedName("channel")
    val channel: List<Int> = listOf(),

    @SerializedName("link")
    val link: List<ShareExAffiliateLinkRequest> = listOf()
)

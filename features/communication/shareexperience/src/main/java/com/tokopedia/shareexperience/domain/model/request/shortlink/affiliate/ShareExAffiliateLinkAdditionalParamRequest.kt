package com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkAdditionalParamRequest(
    @SerializedName("Key")
    val key: String = "",

    @SerializedName("Value")
    val value: String = ""
)

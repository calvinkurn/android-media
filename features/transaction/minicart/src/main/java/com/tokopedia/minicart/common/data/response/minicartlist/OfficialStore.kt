package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class OfficialStore(
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("os_logo_url")
        val osLogoUrl: String = ""
)
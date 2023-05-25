package com.tokopedia.scp_rewards.detail.domain.model

import com.google.gson.annotations.SerializedName

data class SCPRewardsGetDetailPageRequest(
    @SerializedName("pageName")
    val pageName: String = "",
    @SerializedName("medaliSlug")
    val medaliSlug: String = "",
    @SerializedName("sourceName")
    val sourceName: String = ""
)

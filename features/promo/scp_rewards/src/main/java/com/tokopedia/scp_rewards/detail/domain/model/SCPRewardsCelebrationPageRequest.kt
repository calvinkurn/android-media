package com.tokopedia.scp_rewards.celebration.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MedalDetailRequest(
    @Expose
    @SerializedName("apiVersion")
    val apiVersion: String = "",
    @Expose
    @SerializedName("medaliSlug")
    val medaliSlug: String = "",
    @Expose
    @SerializedName("sourceName")
    val sourceName: String = ""
)

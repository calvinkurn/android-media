package com.tokopedia.scp_rewards_touchpoints.bottomsheet.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SCPRewardsCelebrationPageRequest(
    @Expose
    @SerializedName("apiVersion")
    val apiVersion:String = "",
    @Expose
    @SerializedName("medaliSlug")
    val medaliSlug:String = "",
    @Expose
    @SerializedName("sourceName")
    val sourceName:String = ""
)

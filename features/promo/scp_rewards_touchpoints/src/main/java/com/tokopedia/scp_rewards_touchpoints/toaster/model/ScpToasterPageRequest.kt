package com.tokopedia.scp_rewards_touchpoints.toaster.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScpToasterPageRequest(
    @Expose
    @SerializedName("apiVersion")
    val apiVersion:String = "",
    @Expose
    @SerializedName("orderID")
    val orderID:Int = 0,
    @Expose
    @SerializedName("pageName")
    val pageName:String = "",
    @Expose
    @SerializedName("sourceName")
    val sourceName:String = ""
)

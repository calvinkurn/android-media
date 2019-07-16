package com.tokopedia.topads.auto.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsAutoAdsInfo (

    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("message")
    val message: String = ""
)

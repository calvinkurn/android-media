package com.tokopedia.targetedticker.domain

import com.google.gson.annotations.SerializedName

data class TargetedTickerParamModel(
    @SerializedName("page")
    val page: String = "",

    @SerializedName("targets")
    val targets: List<Target> = listOf()
) {
    data class Target(

        @SerializedName("type")
        val type: String = "",

        @SerializedName("value")
        val value: List<String> = listOf()
    )
}

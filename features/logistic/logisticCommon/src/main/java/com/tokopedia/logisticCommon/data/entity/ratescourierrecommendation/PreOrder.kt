package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class PreOrder (
        @SerializedName("header")
        val header: String = "",
        @SerializedName("label")
        val label: String = "",
        @SerializedName("display")
        val display: Boolean = false
)
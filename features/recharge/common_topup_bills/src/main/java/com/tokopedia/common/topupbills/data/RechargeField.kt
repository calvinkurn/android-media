package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeField (
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
)
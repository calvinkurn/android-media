package com.tokopedia.flashsale.management.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Logistic(
        @SerializedName("logistic_id")
        @Expose
        val logisticId: Int = 0,
        @SerializedName("logistic_name")
        @Expose
        val logisticName: String = ""
)
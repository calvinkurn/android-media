package com.tokopedia.recharge_component.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeBUWidgetTrackingData(
        @SerializedName("action")
        @Expose
        val action: String = "",
        @SerializedName("data")
        @Expose
        val data: String = ""
)
package com.tokopedia.recharge_component.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeBUWidgetTracking(
        @SerializedName("action")
        @Expose
        val action: String = "",
        @SerializedName("data")
        @Expose
        val data: String = ""
)
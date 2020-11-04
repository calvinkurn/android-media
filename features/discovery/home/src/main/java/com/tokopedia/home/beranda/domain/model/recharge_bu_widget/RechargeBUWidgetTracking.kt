package com.tokopedia.home.beranda.domain.model.recharge_bu_widget


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
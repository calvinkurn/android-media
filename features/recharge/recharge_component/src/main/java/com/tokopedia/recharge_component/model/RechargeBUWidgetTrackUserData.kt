package com.tokopedia.recharge_component.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeBUWidgetTrackUserData(
    @SerializedName("user_type")
    @Expose
    val userType: String = ""
)

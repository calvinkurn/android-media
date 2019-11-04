package com.tokopedia.v2.home.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeFlag(
    @SerializedName("hasTokopoints")
    @Expose
    var hasTokopoints: Boolean = true,

    @SerializedName("hasRecomNavButton")
    @Expose
    var hasRecomNavButton: Boolean = true
)
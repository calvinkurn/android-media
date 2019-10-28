package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeFlag {
    @SerializedName("hasTokopoints")
    @Expose
    var hasTokopoints: Boolean = true

    @SerializedName("hasRecomNavButton")
    @Expose
    var hasRecomNavButton: Boolean = true
}
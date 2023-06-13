package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LobItem(
    @Expose
    @SerializedName("appLink")
    var appLink: String = "",
    @Expose
    @SerializedName("icon")
    var icon: String = "",
    @Expose
    @SerializedName("url")
    var url: String = "",
    @Expose
    @SerializedName("text")
    var text: String = "",
)

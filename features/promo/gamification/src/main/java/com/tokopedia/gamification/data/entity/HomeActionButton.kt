package com.tokopedia.gamification.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeActionButton(
    @SerializedName("backgroundColor")
    @Expose
    var backgroundColor: String = "" ,

    @SerializedName("text")
    @Expose
    var text: String = "" ,

    @SerializedName("appLink")
    @Expose
    var appLink: String = "",

    @SerializedName("url")
    @Expose
    var url: String = "",

    @SerializedName("type")
    @Expose
    var type: String = ""
)

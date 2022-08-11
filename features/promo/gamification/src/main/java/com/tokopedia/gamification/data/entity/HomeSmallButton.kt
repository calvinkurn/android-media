package com.tokopedia.gamification.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeSmallButton(
    @SerializedName("imageURL")
    @Expose
     var imageURL: String = "",

    @SerializedName("appLink")
@Expose
var appLink: String = "",

@SerializedName("url")
@Expose
var url: String = "",
)
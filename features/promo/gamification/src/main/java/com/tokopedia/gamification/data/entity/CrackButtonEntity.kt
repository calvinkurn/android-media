package com.tokopedia.gamification.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CrackButtonEntity(
    @SerializedName("title")
    @Expose
    var title: String="",

    @SerializedName("url")
@Expose
var url: String="",

@SerializedName("applink")
@Expose
var applink: String="",

@SerializedName("type")
@Expose
var type: String=""
)
package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Shop (
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("applink")
    @Expose
    val applink: String = "",
    @SerializedName("city")
    @Expose
    val city: String = "",
    @SerializedName("reputation")
    @Expose
    val reputation: String = ""
)
package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Authors(

    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("hasUsername")
    var hasUsername: Boolean? = null,
    @SerializedName("hasAcceptTnC")
    var hasAcceptTnC: Boolean? = null,
    @SerializedName("items")
    var items: ArrayList<AuthorItem> = arrayListOf()

)
data class AuthorItem(

    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("applink")
    var applink: String? = null,
    @SerializedName("weblink")
    var weblink: String? = null

)

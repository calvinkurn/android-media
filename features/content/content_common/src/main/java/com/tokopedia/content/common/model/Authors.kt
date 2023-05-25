package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

data class Authors(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("hasUsername")
    val hasUsername: Boolean = false,
    @SerializedName("hasAcceptTnC")
    val hasAcceptTnC: Boolean = false,
    @SerializedName("items")
    val items: List<AuthorItem> = emptyList()

)
data class AuthorItem(

    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("weblink")
    val weblink: String = ""

)

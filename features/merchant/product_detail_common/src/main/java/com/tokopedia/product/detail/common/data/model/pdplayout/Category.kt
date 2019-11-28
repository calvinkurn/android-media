package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("breadcrumbURL")
    val breadcrumbURL: String = "",
    @SerializedName("detail")
    val detail: List<Any> = listOf(),
    @SerializedName("id")
    val id: String = "",
    @SerializedName("isAdult")
    val isAdult: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("title")
    val title: String = ""
)
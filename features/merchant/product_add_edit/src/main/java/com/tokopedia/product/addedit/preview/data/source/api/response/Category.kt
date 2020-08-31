package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("isAdult")
        val isAdult: Boolean = false,
        @SerializedName("breadcrumbURL")
        val breadcrumbURL: String = "",
        @SerializedName("detail")
        val details: List<Detail> = listOf()
)

data class Detail(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("breadcrumbURL")
        val breadcrumbURL: String,
        @SerializedName("isAdult")
        val isAdult: Boolean
)


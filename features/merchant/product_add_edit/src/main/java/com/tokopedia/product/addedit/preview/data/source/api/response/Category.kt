package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("isAdult")
        @Expose
        val isAdult: Boolean = false,
        @SerializedName("breadcrumbURL")
        @Expose
        val breadcrumbURL: String = "",
        @SerializedName("detail")
        @Expose
        val details: List<Detail> = listOf()
)

data class Detail(
        @SerializedName("id")
        @Expose
        val id: String,
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("breadcrumbURL")
        @Expose
        val breadcrumbURL: String,
        @SerializedName("isAdult")
        @Expose
        val isAdult: Boolean
)


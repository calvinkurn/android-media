package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("breadcrumbUrl")
        @Expose
        val breadcrumbUrl: String = "",

        @SerializedName("detail")
        @Expose
        val detail: List<Detail> = listOf(),

        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("title")
        @Expose
        val title: String = ""
){
    data class Detail(
            @SerializedName("breadcrumbUrl")
            @Expose
            val breadcrumbUrl: String = "",

            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = ""
    )
}
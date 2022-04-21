package com.tokopedia.vouchercreation.product.list.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductListMetaResponse(
        @SerializedName("ProductListMeta")
        @Expose val response: Response
) {
    data class Response(
            @Expose
            @SerializedName("header")
            val header: Header = Header(),
            @Expose
            @SerializedName("data")
            val data: Data
    )

    data class Header(
            @Expose
            @SerializedName("processTime")
            val processTime: Float = 0f,
            @Expose
            @SerializedName("messages")
            val messages: List<String> = listOf(),
            @Expose
            @SerializedName("reason")
            val reason: String = "",
            @Expose
            @SerializedName("errorCode")
            val errorCode: String = ""
    )

    data class Data(
            @Expose
            @SerializedName("sort")
            val sort: List<Sort>,
            @Expose
            @SerializedName("shopCategories")
            val category: List<Category>
    )

    data class Sort(
            @Expose
            @SerializedName("id")
            val id: String,
            @Expose
            @SerializedName("value")
            val value: String,
            @Expose
            @SerializedName("name")
            val name: String
    )

    data class Category(
            @Expose
            @SerializedName("id")
            val id: String,
            @Expose
            @SerializedName("value")
            val value: String,
            @Expose
            @SerializedName("name")
            val name: String
    )
}
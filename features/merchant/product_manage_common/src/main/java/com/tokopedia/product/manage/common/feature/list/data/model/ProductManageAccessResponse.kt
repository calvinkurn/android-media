package com.tokopedia.product.manage.common.feature.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductManageAccessResponse(
    @Expose
    @SerializedName("ProductListMeta")
    val response: Response
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
        @SerializedName("access")
        val access: List<Access>
    )

    data class Access(
        @Expose
        @SerializedName("id")
        val id: String
    )
}
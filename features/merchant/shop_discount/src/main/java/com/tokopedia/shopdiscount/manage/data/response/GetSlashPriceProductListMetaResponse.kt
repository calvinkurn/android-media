package com.tokopedia.shopdiscount.manage.data.response


import com.google.gson.annotations.SerializedName

data class GetSlashPriceProductListMetaResponse(
    @SerializedName("GetSlashPriceProductListMeta")
    val getSlashPriceProductListMeta: GetSlashPriceProductListMeta = GetSlashPriceProductListMeta()
) {
    data class GetSlashPriceProductListMeta(
        @SerializedName("data")
        val `data`: Data = Data(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class Data(
            @SerializedName("tab")
            val tab: List<Tab> = listOf()
        ) {
            data class Tab(
                @SerializedName("id")
                val id: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("value")
                val value: Int = 0
            )
        }

        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("error_message")
            val errorMessage: List<Any> = listOf(),
            @SerializedName("process_time")
            val processTime: Double = 0.0,
            @SerializedName("reason")
            val reason: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}
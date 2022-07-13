package com.tokopedia.shopdiscount.manage.data.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

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
    }
}
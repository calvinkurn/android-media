package com.tokopedia.vouchergame.list.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListData(

        @SerializedName("componentID")
        @Expose
        val componentID: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("paramName")
        @Expose
        val paramName: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("operators")
        @Expose
        var operators: List<VoucherGameOperator> = listOf()

) {
        class Response(
                @SerializedName("rechargeCatalogOperatorSelect")
                @Expose
                val response: VoucherGameListData = VoucherGameListData()
        )
}
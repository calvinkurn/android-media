package com.tokopedia.oneclickcheckout.order.data.creditcard

import com.google.gson.annotations.SerializedName

data class CreditCardTenorListResponse(
        @SerializedName("creditCardTenorList")
        val ccTenorList: CreditCardTenorList = CreditCardTenorList()
)

data class CreditCardTenorList(
        @SerializedName("error_msg")
        val errorMsg: String = "",

        @SerializedName("error_code")
        val errorCode: String = "",

        @SerializedName("tenor_list")
        val tenorList: List<TenorListItem> = emptyList(),

        @SerializedName("process_time")
        val processTime: String = ""
)

data class TenorListItem(
        @SerializedName("amount")
        val amount: Double = 0.0,

        @SerializedName("bank")
        val bank: String = "",

        @SerializedName("rate")
        val rate: Double = 0.0,

        @SerializedName("fee")
        val fee: Double = 0.0,

        @SerializedName("description")
        val description: String = "",

        @SerializedName("disabled")
        val disabled: Boolean = false,

        @SerializedName("type")
        val type: String = ""
)

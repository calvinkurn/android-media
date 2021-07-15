package com.tokopedia.cart_common.data.response.deletecart

import com.google.gson.annotations.SerializedName
import com.tokopedia.cart_common.data.response.deletecart.Data

data class RemoveFromCartData(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("data")
        val data: Data = Data()
)

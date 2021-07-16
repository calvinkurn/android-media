package com.tokopedia.cart_common.data.response.updatecart

import com.google.gson.annotations.SerializedName

data class UpdateCartV2Data(
        @SerializedName("error_message")
        val error: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: Data = Data()
)
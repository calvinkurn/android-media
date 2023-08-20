package com.tokopedia.cartrevamp.domain.model.bmgm.response

import com.google.gson.annotations.SerializedName

data class BmGmGetGroupProductTickerResponse(
    @SerializedName("error_message")
    val errorMessage: List<String> = emptyList(),

    @SerializedName("data")
    val data: Data = Data(),

    @SerializedName("status")
    val status: String = ""
) {
    data class Data(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("action")
        val action: String = ""
    )
}

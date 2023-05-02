package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class LsPrintData(
    @SerializedName("oiaction")
    val oiaction: Oiaction = Oiaction()
) {
    data class Oiaction(
        @SerializedName("error")
        val error: String = "",

        @SerializedName("status")
        val status: Int = -1,

        @SerializedName("data")
        val data: Data = Data()
    ) {
        data class Data(
            @SerializedName("message")
            val message: String = ""
        )
    }
}

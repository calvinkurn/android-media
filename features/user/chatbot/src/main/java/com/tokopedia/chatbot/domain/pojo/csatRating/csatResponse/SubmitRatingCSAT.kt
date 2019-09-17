package com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse

import com.google.gson.annotations.SerializedName

data class SubmitRatingCSAT(


        @SerializedName("data")
        val data: Data? = null,


        @SerializedName("header")
        val header: Header? = null
) {
    data class Data(
            @SerializedName("message")
            val message: String
    )
}
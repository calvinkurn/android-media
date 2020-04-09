package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddToCartOccResponse(
        @SerializedName("error_message")
        @Expose
        val errorMessage: ArrayList<String> = arrayListOf(),

        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("data")
        @Expose
        val data: DataOccResponse = DataOccResponse()
)
package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataOccResponse(
        @SerializedName("success")
        @Expose
        val success: Int = 0,

        @SerializedName("message")
        @Expose
        val message: ArrayList<String> = arrayListOf(),

        @SerializedName("data")
        @Expose
        val detail: DetailOccResponse = DetailOccResponse()
)
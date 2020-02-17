package com.tokopedia.purchase_platform.common.data.model.response.helpticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitTicketDataResponse(
        @SerializedName("success")
        @Expose
        val success: Int = 0,
        @SerializedName("message")
        @Expose
        val message: ArrayList<String> = arrayListOf(),
        @SerializedName("texts")
        @Expose
        val texts: SubmitTicketTextResponse = SubmitTicketTextResponse()
)
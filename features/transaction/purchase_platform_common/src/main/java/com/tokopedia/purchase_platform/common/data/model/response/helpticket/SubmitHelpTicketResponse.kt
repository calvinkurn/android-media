package com.tokopedia.purchase_platform.common.data.model.response.helpticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitHelpTicketResponse(
        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("error_messages")
        @Expose
        val errorMessages: ArrayList<String> = arrayListOf(),

        @SerializedName("data")
        @Expose
        val data: SubmitTicketDataResponse = SubmitTicketDataResponse()
)
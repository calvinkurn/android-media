package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class CreateRequestResponse(
    @SerializedName("requestQueueRefId")
    val requestQueueRefId: String = "",
    @SerializedName("requestQueueId")
    val requestQueueId: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("deadline")
    val deadline: String = ""
)

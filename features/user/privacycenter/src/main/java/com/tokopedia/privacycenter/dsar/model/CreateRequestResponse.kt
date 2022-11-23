package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName

data class CreateRequestResponse(
    @SerializedName("requestQueueRefId")
    val requestQueueRefId: String = "",
    @SerializedName("requestQueueId")
    val requestQueueId: String = ""
)

package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class GetRequestDetailResponse(
    @SerializedName("requestQueueRefId")
    val requestQueueRefId: String = "",
    @SerializedName("requestQueueId")
    val requestQueueId: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("deadline")
    val deadline: String = "",
    @SerializedName("additionalData")
    val additionalData: String = ""
)

data class RequestAdditionalData(
    @SerializedName("Loyalty ID")
    val userId: String = "",
    @SerializedName("Request Details")
    val requestDetails: String = "",
    @SerializedName("Phone Number")
    val phoneNumber: String = ""
)

package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class CreateRequestBody(
    @SerializedName("language")
    var languange: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("requestTypes")
    var requestType: List<String> = listOf("Info Request"),
    @SerializedName("subjectTypes")
    var subjectType: List<String> = listOf("Customer"),
    @SerializedName("additionalData")
    var additionalData: AdditionalData = AdditionalData(),
    @SerializedName("firstName")
    var firstName: String = "",
    @SerializedName("lastName")
    var lastName: String = "",
    @Transient
    var templateId: String
)

data class AdditionalData(
    @SerializedName("loyaltyId")
    val userId: String = "",
    @SerializedName("requestDetails")
    val requestDetails: String = "",
    @SerializedName("phoneNumber")
    val phoneNumber: String = ""
)

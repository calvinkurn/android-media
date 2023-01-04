package com.tokopedia.minicart.common.promo.data.response

import com.google.gson.annotations.SerializedName

class ValidateUseMvcGqlResponse(
    @SerializedName("validate_use_mvc")
    val response: ValidateUseMvcResponse = ValidateUseMvcResponse()
)

class ValidateUseMvcResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("message")
    val message: List<String> = emptyList(),
    @SerializedName("error_code")
    val errorCode: String = "",
    @SerializedName("data")
    val data: ValidateUseMvcData = ValidateUseMvcData()
)

class ValidateUseMvcData(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("curr_purchase")
    val currentPurchase: Double = 0.0,
    @SerializedName("min_purchase")
    val minimumPurchase: Double = 0.0,
    @SerializedName("progress_percentage")
    val progressPercentage: Int = 0,
    @SerializedName("message")
    val message: String = ""
)

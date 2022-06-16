package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ChangePinV2param(
    @SerializedName("pin")
    val pin: String = "",
    @SerializedName("pin_old")
    val oldPin: String = "",
    @SerializedName("pin_confirm")
    val confirmPin: String = "",
    @SerializedName("h")
    val hash: Boolean = false
): GqlParam

data class MutatePinV2Data(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("errors")
    val errors: List<ErrorPinModel> = listOf()
)
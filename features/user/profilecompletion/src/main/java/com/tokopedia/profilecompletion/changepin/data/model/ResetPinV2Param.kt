package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ResetPinV2Param(
    @SerializedName("pin")
    val pin: String = "",
    @SerializedName("pin_confirm")
    val confirmPin: String = "",
    @SerializedName("validate_token")
    val validateToken: String = "",
    @SerializedName("h")
    val hash: Boolean = false
): GqlParam

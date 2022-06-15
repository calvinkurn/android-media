package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class CreatePinV2Param(
    @SerializedName("pin")
    val pin: String,
    @SerializedName("pin_confirm")
    val confirmPin: String,
    @SerializedName("validate_token")
    val validateToken: String,
    @SerializedName("h")
    val hash: String
): GqlParam


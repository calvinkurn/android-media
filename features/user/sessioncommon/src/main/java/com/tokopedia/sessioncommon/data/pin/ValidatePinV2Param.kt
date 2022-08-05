package com.tokopedia.sessioncommon.data.pin

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ValidatePinV2Param(
    @SerializedName("pin")
    val pin: String,
    @SerializedName("h")
    val hash: String
): GqlParam
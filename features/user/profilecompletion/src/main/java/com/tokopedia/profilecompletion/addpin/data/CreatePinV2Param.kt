package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class CreatePinV2Param(
    @SerializedName("pin_token")
    val pin_token: String,
    @SerializedName("validate_token")
    val validateToken: String,
) : GqlParam


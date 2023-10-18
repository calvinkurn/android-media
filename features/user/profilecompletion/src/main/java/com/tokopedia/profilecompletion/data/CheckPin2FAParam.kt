package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class CheckPin2FAParam(
    @SerializedName("pin")
    val pin: String = "",

    @SerializedName("validate_token")
    val validateToken: String = "",

    @SerializedName("action")
    val action: String = "",

    @SerializedName("user_id")
    val userId: Int = 0
): GqlParam

package com.tokopedia.profilecompletion.common.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class CheckPinV2Param(
    @SerializedName("pin")
    val pin: String,
    @SerializedName("h")
    val hash: String,
    @SerializedName("action")
    val action: String = "",
    @SerializedName("validate_token")
    val validateToken: String = "",
    // Suppress lint because BE only accept INT value
    @SuppressLint("Invalid Data Type")
    @SerializedName("user_id")
    val userId: Int = 0
) : GqlParam

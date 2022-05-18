package com.tokopedia.profilecompletion.common.model

import com.google.gson.annotations.SerializedName

data class CheckPinV2Param(
    @SerializedName("pin")
    val pin: String,
    @SerializedName("h")
    val hash: String,
    @SerializedName("action")
    val action: String = "",
    @SerializedName("validate_token")
    val validateToken: String = "",
    @SerializedName("user_id")
    val userId: String = ""
)
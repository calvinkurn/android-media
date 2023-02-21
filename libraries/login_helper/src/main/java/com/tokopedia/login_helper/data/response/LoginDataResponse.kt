package com.tokopedia.login_helper.data.response

import com.google.gson.annotations.SerializedName

data class LoginDataResponse(
    @SerializedName("count")
    val count: Int? = 0,
    @SerializedName("users")
    val users: List<UserDataResponse>? = null
)

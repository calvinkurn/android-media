package com.tokopedia.loginregister.goto_seamless.model

import com.google.gson.annotations.SerializedName

data class GetNameResponse(
    @SerializedName("sso_get_username")
    var data: GetNameData = GetNameData()
)

data class GetNameData(
    @SerializedName("fullname")
    var name: String = "",
    @SerializedName("error")
    var error: String = ""
)

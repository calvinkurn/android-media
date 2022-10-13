package com.tokopedia.loginregister.goto_seamless.model

import com.google.gson.annotations.SerializedName

data class GetNameResponse(
    @SerializedName("fullname")
    var name: String = "",
    @SerializedName("error")
    var error: String = ""
)

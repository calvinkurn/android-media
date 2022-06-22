package com.tokopedia.loginregister.goto_seamless.model

import com.google.gson.annotations.SerializedName

data class TempKeyResponse(
    @SerializedName("generate_key")
    val data: TempKeyData = TempKeyData()
)

data class TempKeyData(
    @SerializedName("key")
    var key: String = ""
)
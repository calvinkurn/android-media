package com.tokopedia.loginregister.redefine_register_email.domain.data

import com.google.gson.annotations.SerializedName

data class GenerateKeyModel(
    @SerializedName("generate_key")
    var keyData: KeyData = KeyData()
)

data class KeyData(
    @SerializedName("key")
    var key: String = "",

    @SerializedName("server_timestamp")
    var timestamp: String = "",

    @SerializedName("h")
    var hash: String = ""
)
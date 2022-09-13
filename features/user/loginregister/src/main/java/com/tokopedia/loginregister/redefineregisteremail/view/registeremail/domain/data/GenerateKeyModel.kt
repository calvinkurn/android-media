package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data

import com.google.gson.annotations.SerializedName

data class GenerateKeyModel(
    @SerializedName("generate_key")
    val generateKey: GenerateKey = GenerateKey()
)

data class GenerateKey(
    @SerializedName("key")
    val key: String = "",

    @SerializedName("server_timestamp")
    val serverTimestamp: String = "",

    @SerializedName("h")
    val hash: String = ""
)
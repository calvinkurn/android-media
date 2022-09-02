package com.tokopedia.loginregister.redefine_register_email.register_email.domain.data

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
    val h: String = ""
)
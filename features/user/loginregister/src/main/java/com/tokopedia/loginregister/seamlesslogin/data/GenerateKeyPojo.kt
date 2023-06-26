package com.tokopedia.loginregister.seamlesslogin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 17/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class GenerateKeyPojo {
    @SerializedName("generate_key")
    @Expose
    var data: GenerateKeyData = GenerateKeyData()
}

data class GenerateKeyData(
    @SerializedName("key")
    var key: String = "",
    @SerializedName("server_timestamp")
    var server_timestamp: String = "",
    @SerializedName("error")
    var error: String = "",

    @Transient
    var taskId: String = ""
)

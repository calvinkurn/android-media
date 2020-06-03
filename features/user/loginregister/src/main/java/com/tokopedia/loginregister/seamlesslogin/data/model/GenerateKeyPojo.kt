package com.tokopedia.loginregister.seamlesslogin.data.model

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
        var key: String = "",
        var server_timestamp: String = "",
        var error: String = "",

        @Transient
        var taskId: String = ""
)
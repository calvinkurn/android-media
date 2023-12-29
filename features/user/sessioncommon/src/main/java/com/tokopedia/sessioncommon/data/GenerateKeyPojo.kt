package com.tokopedia.sessioncommon.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

data class GenerateKeyPojo(
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

package com.tokopedia.seamless_login_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2019-11-07.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */


data class KeyResponse(
        @SerializedName("generate_key")
        @Expose
        val data: KeyPojo
)

data class KeyPojo (
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("server_timestamp")
        @Expose
        val timestamp: Long = 0L
)

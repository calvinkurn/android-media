package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2020-02-11.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ValidateFingerprintResult(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,

        @SerializedName("validateToken")
        @Expose
        var validateToken: String = "",

        @SerializedName("message")
        @Expose
        var message: String = "",

        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = ""

)
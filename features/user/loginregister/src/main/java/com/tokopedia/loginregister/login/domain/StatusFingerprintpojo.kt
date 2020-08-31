package com.tokopedia.loginregister.login.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class StatusFingerprintpojo(
        @SerializedName("VerifyFingerprint")
        @Expose
        var data: StatusFingerprint = StatusFingerprint()
)

data class StatusFingerprint(
        @SerializedName("isValid")
        @Expose
        var isValid: Boolean = false,

        @SerializedName("message")
        @Expose
        var message: String = "",

        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = ""
)
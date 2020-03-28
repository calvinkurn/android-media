package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class VerifyFingerprintPojo(
        @SerializedName("VerifyFingerprint")
        var data: VerifyFingerprint = VerifyFingerprint()
)

data class VerifyFingerprint(
        @SerializedName("isValid")
        var isValid: Boolean = false,

        @SerializedName("message")
        var message: String = "",

        @SerializedName("errorMessage")
        var errorMessage: String = ""
)
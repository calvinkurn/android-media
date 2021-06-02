package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class VerifyFingerprintPojo(
        @SerializedName("OtpVerifyBiometric")
        var data: VerifyFingerprint = VerifyFingerprint()
)

data class VerifyFingerprint(
        @SerializedName("isSuccess")
        var isSuccess: Boolean = false,

        @SerializedName("status")
        var status: String = "",

        @SerializedName("validateToken")
        var validateToken: String = "",

        @SerializedName("errorMessage")
        var errorMessage: String = ""
)
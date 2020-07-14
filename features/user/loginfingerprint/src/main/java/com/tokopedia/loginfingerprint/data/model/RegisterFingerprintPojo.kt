package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 2020-02-06.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class RegisterFingerprintPojo(
        @SerializedName("RegisterFingerprint")
        @Expose
        var data: RegisterFingerprintResult = RegisterFingerprintResult()
)
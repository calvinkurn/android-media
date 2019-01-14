package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class PhoneDataEntity(
        @SerializedName("mobile_device_id")
        var mobileDeviceId: Int = 0
)

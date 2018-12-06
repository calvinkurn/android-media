package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

class PhoneDataEntity {
    @SerializedName("mobile_device_id")
    var mobileDeviceId: Int = 0

    override fun toString(): String {
        return "PhoneDataEntity{" +
                "mobileDeviceId=" + mobileDeviceId +
                '}'.toString()
    }
}

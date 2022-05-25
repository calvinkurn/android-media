package com.tokopedia.profilecompletion.common.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.profilecompletion.addpin.data.CheckPinData

data class CheckPinV2Response(
    @SerializedName("check_pin_v2")
    val data: CheckPinData = CheckPinData()
)

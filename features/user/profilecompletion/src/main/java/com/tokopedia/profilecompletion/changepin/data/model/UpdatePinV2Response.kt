package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData

data class UpdatePinV2Response(
    @SerializedName("update_pin_v2")
    val mutatePinV2data: AddChangePinData = AddChangePinData()
)
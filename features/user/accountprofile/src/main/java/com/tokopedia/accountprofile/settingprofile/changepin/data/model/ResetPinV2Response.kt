package com.tokopedia.accountprofile.settingprofile.changepin.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.accountprofile.settingprofile.addpin.data.AddChangePinData

data class ResetPinV2Response(
    @SerializedName("reset_pin_v2")
    val mutatePinV2data: AddChangePinData = AddChangePinData()
)

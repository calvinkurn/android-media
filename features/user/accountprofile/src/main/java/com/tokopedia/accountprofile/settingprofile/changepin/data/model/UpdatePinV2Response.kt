package com.tokopedia.accountprofile.settingprofile.changepin.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.accountprofile.settingprofile.addpin.data.AddChangePinData

data class UpdatePinV2Response(
    @SerializedName("update_pin_v2")
    val mutatePinV2data: AddChangePinData = AddChangePinData()
)

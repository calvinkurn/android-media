package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName

data class UpdatePinV2Response(
    @SerializedName("update_pin_v2")
    val mutatePinV2data: MutatePinV2Data = MutatePinV2Data()
)
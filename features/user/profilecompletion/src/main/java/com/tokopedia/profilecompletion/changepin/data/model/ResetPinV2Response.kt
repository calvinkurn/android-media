package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName

data class ResetPinV2Response(
    @SerializedName("reset_pin_v2")
    val mutatePinV2data: MutatePinV2Data = MutatePinV2Data()
)

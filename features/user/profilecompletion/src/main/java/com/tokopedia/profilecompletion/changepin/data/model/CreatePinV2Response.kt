package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName

data class CreatePinV2Response(
    @SerializedName("create_pin_v2")
    val mutatePinV2data: MutatePinV2Data = MutatePinV2Data()
)
package com.tokopedia.kyc_centralized.data.model

import com.google.gson.annotations.SerializedName

data class KycData(
    @SerializedName("is_success_register")
    var isSuccessRegister: Boolean = false,
    @SerializedName("list_retake")
    var listRetake: ArrayList<Int> = ArrayList(),
    @SerializedName("list_message")
    var listMessage: ArrayList<String> = ArrayList(),
    @SerializedName("apps")
    var app: KycAppModel = KycAppModel(),
)

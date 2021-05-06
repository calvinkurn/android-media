package com.tokopedia.kyc_centralized.data.model.response

import com.google.gson.annotations.SerializedName

data class KycResponse (
    @SerializedName("data")
    var data: KycData = KycData()
)
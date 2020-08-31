package com.tokopedia.liveness.data.model.response

import com.google.gson.annotations.SerializedName

data class LivenessResponse (
    @SerializedName("data")
    var data: LivenessData = LivenessData()
)
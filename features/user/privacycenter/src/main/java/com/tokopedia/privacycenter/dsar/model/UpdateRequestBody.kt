package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName

data class UpdateRequestBody(
    @SerializedName("nextStage")
    var nextStage: String = "SVP Approval",
)

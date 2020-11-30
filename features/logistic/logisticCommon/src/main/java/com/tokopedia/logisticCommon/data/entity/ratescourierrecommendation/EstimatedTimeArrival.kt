package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

class EstimatedTimeArrival(
        @SerializedName("text_eta")
        val textEta: String = "",
        @SerializedName("error_code")
        val errorCode: Int = -1
)
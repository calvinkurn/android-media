package com.tokopedia.rechargeocr.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeOcrResponse(
        @SerializedName("rechargeOCR")
        @Expose
        val rechargeOcr: ResultOcr = ResultOcr()
)

class ResultOcr(
    @SerializedName("result")
    @Expose
    val result: String = "")
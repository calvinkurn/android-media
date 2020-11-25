package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 15/07/20
 */

data class ChartSummaryModel(
        @Expose
        @SerializedName("diffPercentage")
        val diffPercentage: Int = 0,
        @Expose
        @SerializedName("diffPercentageFmt")
        val diffPercentageFmt: String = "",
        @Expose
        @SerializedName("value")
        val value: Int = 0,
        @Expose
        @SerializedName("valueFmt")
        val valueFmt: String = ""
)
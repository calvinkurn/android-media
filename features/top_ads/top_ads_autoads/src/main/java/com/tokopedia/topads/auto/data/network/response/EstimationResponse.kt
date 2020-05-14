package com.tokopedia.topads.auto.data.network.response

import com.google.gson.annotations.SerializedName

data class EstimationResponse(

        @field:SerializedName("topadsStatisticsEstimationAttribute")
        val topadsStatisticsEstimationAttribute: TopadsStatisticsEstimationAttribute = TopadsStatisticsEstimationAttribute()
) {
    data class TopadsStatisticsEstimationAttribute(

            @field:SerializedName("data")
            val data: List<DataItem> = listOf()
    ) {
        data class DataItem(

                @field:SerializedName("lowClickDivider")
                val lowClickDivider: Int = 0,

                @field:SerializedName("lowImpMultiplier")
                val lowImpMultiplier: Double = 0.00,

                @field:SerializedName("highImpMultiplier")
                val highImpMultiplier: Double = 0.00,

                @field:SerializedName("highClickDivider")
                val highClickDivider: Int = 0,

                @field:SerializedName("type")
                val type: Int = 0
        )
    }
}
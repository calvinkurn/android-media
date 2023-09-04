package com.tokopedia.topads.data


import com.google.gson.annotations.SerializedName

data class ImpressionPredictionResponse(
    @SerializedName("umpGetImpressionPredictionSearch", alternate = ["umpGetImpressionPredictionBrowse"])
    val umpGetImpressionPrediction: UmpGetImpressionPrediction
) {
    data class UmpGetImpressionPrediction(
        @SerializedName("data")
        val impressionPredictionData: ImpressionPredictionData,
        @SerializedName("error")
        val error: Error
    ) {
        data class ImpressionPredictionData(
            @SerializedName("impression")
            val impression: Impression
        ) {
            data class Impression(
                @SerializedName("finalImpression")
                val finalImpression: Int,
                @SerializedName("increment")
                val increment: Int,
                @SerializedName("oldImpression")
                val oldImpression: Int
            )
        }
        data class Error(
            @SerializedName("title")
            val title: String
        )
    }
}

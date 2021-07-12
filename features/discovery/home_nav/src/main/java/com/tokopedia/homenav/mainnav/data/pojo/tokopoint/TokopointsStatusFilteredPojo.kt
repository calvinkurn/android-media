package com.tokopedia.homenav.mainnav.data.pojo.tokopoint

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 3/1/21.
 */
data class TokopointsStatusFilteredPojo (
    @SerializedName("tokopointsStatusFiltered")
    val tokopointsStatusFiltered: TokopointsStatusFiltered = TokopointsStatusFiltered()
){

    data class TokopointsStatusFiltered(
        @SerializedName("statusFilteredData")
        val statusFilteredData: StatusFilteredData = StatusFilteredData()
    )

    data class StatusFilteredData(
        @SerializedName("points")
        val points: Points = Points()
    )

    data class Points(
            @SerializedName("iconImageURL")
            val iconImageURL: String = "",
            @SerializedName("pointsAmount")
            val pointsAmount: Int = 0,
            @SerializedName("pointsAmountStr")
            val pointsAmountStr: String = "",
            @SerializedName("externalCurrencyAmount")
            val externalCurrencyAmount: Int = 0,
            @SerializedName("externalCurrencyAmountStr")
            val externalCurrencyAmountStr: String = ""
    )
}
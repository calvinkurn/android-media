package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class Texts(
        @SerializedName("bottom_sheet")
        val bottomSheet: String = "",
        @SerializedName("chosen_courier")
        val chosenCourier: String = "",
        @SerializedName("ticker_courier")
        val tickerCourier: String = "",
        @SerializedName("bottom_sheet_description")
        val bottomSheetDescription: String = ""
)
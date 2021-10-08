package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class Texts(
        @SerializedName("bottom_sheet")
        val bottomSheet: String = "",
        @SerializedName("chosen_courier")
        val chosenCourier: String = "<b>Bebas Ongkir</b>",
        @SerializedName("strikethrough_price")
        val strikethroughPrice: String = "<b>(</b><s>Rp.40.000</s> <b>Rp 0)</b>"
)
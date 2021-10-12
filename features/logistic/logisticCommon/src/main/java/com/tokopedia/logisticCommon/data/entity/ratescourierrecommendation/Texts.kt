package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class Texts(
        @SerializedName("bottom_sheet")
        val bottomSheet: String = "<b>Bebas Ongkir Khusus Surabaya</b>",
        @SerializedName("chosen_courier")
        val chosenCourier: String = "<b>Bebas Ongkir (</b><s>Rp.40.000</s> <b>Rp 0)</b>",
        @SerializedName("ticker_courier")
        val tickerCourier: String = "<b>Tersedia Bebas Ongkir OCC</b> <s>Gratis</s>"
)
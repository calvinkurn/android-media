package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class InsertTradeInLogisticPreferenceInput(
    @SerializedName("DeviceId")
    var deviceId: String,
    @SerializedName("FinalPrice")
    var finalPrice: Double,
    @SerializedName("Imei")
    var imei: String,
    @SerializedName("UniqueCode")
    var uniqueCode: String,
    @SerializedName("CampaignTagId")
    var campaignTagId: String,
    @SerializedName("Is3PL")
    var is3PL: Boolean,
    @SerializedName("TradeInPrice")
    var tradeInPrice: Double
)
package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class InsertTradeInLogisticPreferenceInput(
    @SerializedName("DeviceAttr")
    var deviceAttr: DeviceAttr,
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
) {
    data class DeviceAttr(
        @SerializedName("Brand")
        var brand: String,
        @SerializedName("Grade")
        var grade: String,
        @SerializedName("Imei")
        var imei: List<String>,
        @SerializedName("Model")
        var model: String,
        @SerializedName("ModelId")
        var modelId: Int,
        @SerializedName("Ram")
        var ram: String,
        @SerializedName("Storage")
        var storage: String
    )
}
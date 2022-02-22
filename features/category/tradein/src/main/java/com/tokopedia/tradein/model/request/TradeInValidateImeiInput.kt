package com.tokopedia.tradein.model.request


import com.google.gson.annotations.SerializedName

data class TradeInValidateImeiInput(
    @SerializedName("AppDeviceId")
    var appDeviceId: String,
    @SerializedName("Imei")
    var imei: String,
    @SerializedName("ModelInfo")
    var modelInfo: String,
    @SerializedName("SessionId")
    var sessionId: String,
    @SerializedName("TraceId")
    var traceId: String
)
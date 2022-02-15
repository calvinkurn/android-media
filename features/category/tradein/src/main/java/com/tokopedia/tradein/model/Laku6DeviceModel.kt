package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class Laku6DeviceModel(
    @SerializedName("app_version")
    var appVersion: String,
    @SerializedName("brand")
    var brand: String,
    @SerializedName("campaign_id")
    var campaignId: String,
    @SerializedName("device")
    var device: String,
    @SerializedName("device_signature")
    var deviceSignature: String,
    @SerializedName("model")
    var model: String,
    @SerializedName("os_name")
    var osName: String,
    @SerializedName("os_version")
    var osVersion: String,
    @SerializedName("raw_ram")
    var rawRam: Int,
    @SerializedName("raw_storage")
    var rawStorage: String,
    @SerializedName("root_detected")
    var rootDetected: Boolean,
    @SerializedName("serial")
    var serial: String,
    @SerializedName("session_id")
    var sessionId: String,
    @SerializedName("skip_checking_price")
    var skipCheckingPrice: Boolean,
    @SerializedName("tokopedia_test_type")
    var tokopediaTestType: String,
    @SerializedName("trace_id")
    var traceId: String
)
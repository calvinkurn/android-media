package com.tokopedia.common_tradein.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class DeviceDiagnostics {
    @SerializedName("trade_in_unique_code")
    @Expose
    var tradeInUniqueCode: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("model_id")
    @Expose
    var modelId: Int? = null

    @SerializedName("model")
    @Expose
    var model: String? = null

    @SerializedName("brand")
    @Expose
    var brand: String? = null

    @SerializedName("storage")
    @Expose
    var storage: String? = null

    @SerializedName("ram")
    @Expose
    var ram: String? = null

    @SerializedName("imei")
    @Expose
    var imei: String? = null

    @SerializedName("trade_in_price")
    @Expose
    var tradeInPrice: Int? = null

    @SerializedName("grade")
    @Expose
    var grade: String? = null

    @SerializedName("timestamp")
    @Expose
    var timestamp: Int? = null

    @SerializedName("review_details")
    @Expose
    var reviewDetails: List<String>? = null
}
package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCheckPromoCode {

    @SerializedName("config")
    @Expose
    var config: Any? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null
    @SerializedName("server_process_time")
    @Expose
    var serverProcessTime: Double = 0.toDouble()
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("error_message")
    @Expose
    var errorMessage: List<Any>? = null

}

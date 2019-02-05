package com.tokopedia.promocheckout.common.domain.model.cancelpromo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Header {

    @SerializedName("process_time")
    @Expose
    var processTime: Double = 0.toDouble()
    @SerializedName("messages")
    @Expose
    var messages: List<Any>? = null
    @SerializedName("reason")
    @Expose
    var reason: String? = null
    @SerializedName("error_code")
    @Expose
    var errorCode: String? = null

}

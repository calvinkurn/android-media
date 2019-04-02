package com.tokopedia.payment.setting.authenticate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckWhiteListStatus {

    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("status_code")
    @Expose
    var statusCode: Int = 0
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

}

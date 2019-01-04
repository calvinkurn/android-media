package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCheckPromoCode {
    @SerializedName("data")
    @Expose
    var data: Data? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("error_message")
    @Expose
    var errorMessage: List<String>? = null

}

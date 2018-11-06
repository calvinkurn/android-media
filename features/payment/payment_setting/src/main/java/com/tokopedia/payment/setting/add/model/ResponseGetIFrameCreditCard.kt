package com.tokopedia.payment.setting.add.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGetIFrameCreditCard {

    @SerializedName("success")
    @Expose
    var isSuccess: Boolean = false
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null

}

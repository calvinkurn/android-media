package com.tokopedia.payment.setting.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RemoveCreditCard {

    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("success")
    @Expose
    var isSuccess: Boolean = false

}

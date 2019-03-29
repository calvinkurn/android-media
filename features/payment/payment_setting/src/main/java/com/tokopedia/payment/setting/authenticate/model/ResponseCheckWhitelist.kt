package com.tokopedia.payment.setting.authenticate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCheckWhitelist {

    @SerializedName("data")
    @Expose
    var data: Data? = null

}

package com.tokopedia.payment.setting.authenticate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("state")
    @Expose
    var state: Int = 0
    @SerializedName("user_email")
    @Expose
    var userEmail: String? = null

}

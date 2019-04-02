package com.tokopedia.payment.setting.add.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Headers {

    @SerializedName("Content-Type")
    @Expose
    var contentType: String? = null

}

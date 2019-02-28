package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pin {

    @SerializedName("need_pin")
    @Expose
    var isNeedPin: Boolean = false
    @SerializedName("text")
    @Expose
    var text: String? = ""

}

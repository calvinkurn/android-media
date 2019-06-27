package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Swipe {

    @SerializedName("need_swipe")
    @Expose
    var isNeedSwipe: Boolean = false
    @SerializedName("text")
    @Expose
    var text: String = ""
    @SerializedName("note")
    @Expose
    var note: String = ""
    @SerializedName("partner_code")
    @Expose
    var partnerCode: String = ""
    @SerializedName("pin")
    @Expose
    var pin: Pin = Pin()

}

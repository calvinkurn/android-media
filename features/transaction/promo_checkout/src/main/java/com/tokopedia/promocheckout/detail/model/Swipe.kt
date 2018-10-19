package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Swipe {

    @SerializedName("need_swipe")
    @Expose
    var isNeedSwipe: Boolean = false
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("partner_code")
    @Expose
    var partnerCode: String? = null
    @SerializedName("pin")
    @Expose
    var pin: Pin? = null

}

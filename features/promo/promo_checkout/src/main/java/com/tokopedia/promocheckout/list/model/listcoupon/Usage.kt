package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Usage {

    @SerializedName("activeCoupon")
    @Expose
    var activeCoupon: Int = 0
    @SerializedName("expiredCountdown")
    @Expose
    var expiredCountdown: Int = 0
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("usageStr")
    @Expose
    var usageStr: String? = null
    @SerializedName("buttonUsage")
    @Expose
    var buttonUsage: ButtonUsage? = null
    @SerializedName("activeCountdown")
    @Expose
    var activeCountdown: Int = 0

}

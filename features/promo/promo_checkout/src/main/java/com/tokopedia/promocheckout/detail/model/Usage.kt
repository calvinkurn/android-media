package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Usage {

    @SerializedName("active_count_down")
    @Expose
    var activeCountDown: Int = 0
    @SerializedName("expired_count_down")
    @Expose
    var expiredCountDown: Int = 0
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("usage_str")
    @Expose
    var usageStr: String? = null
    @SerializedName("btn_usage")
    @Expose
    var btnUsage: BtnUsage? = null

}

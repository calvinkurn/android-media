package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BtnUsage {

    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("applink")
    @Expose
    var applink: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null

}

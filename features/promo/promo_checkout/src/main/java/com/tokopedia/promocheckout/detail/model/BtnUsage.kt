package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BtnUsage {

    @SerializedName("text")
    @Expose
    var text: String = ""
    @SerializedName("url")
    @Expose
    var url: String = ""
    @SerializedName("applink")
    @Expose
    var applink: String = ""
    @SerializedName("type")
    @Expose
    var type: String = ""

}

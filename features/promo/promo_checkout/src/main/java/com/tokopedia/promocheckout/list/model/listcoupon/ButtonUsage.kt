package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ButtonUsage {

    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("appLink")
    @Expose
    var appLink: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null

}

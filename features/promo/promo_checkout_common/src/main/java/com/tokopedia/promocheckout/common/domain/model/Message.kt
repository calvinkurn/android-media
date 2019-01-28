package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message {

    @SerializedName("state")
    @Expose
    var state: String? = null
    @SerializedName("color")
    @Expose
    var color: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null

}

package com.tokopedia.common_digital.atc.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestBodyAtcDigital {

    @SerializedName("type")
    @Expose
    var type: String = ""

    @SerializedName("attributes")
    @Expose
    var attributes: Attributes = Attributes()
}

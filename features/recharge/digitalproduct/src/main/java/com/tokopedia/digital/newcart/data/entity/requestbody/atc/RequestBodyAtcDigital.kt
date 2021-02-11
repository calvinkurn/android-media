package com.tokopedia.digital.newcart.data.entity.requestbody.atc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 27/08/18.
 */
class RequestBodyAtcDigital {

    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("attributes")
    @Expose
    var attributes: Attributes? = null
}

package com.tokopedia.promocheckout.common.domain.model.cancelpromo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCancelPromo {

    @SerializedName("header")
    @Expose
    var header: Header? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}

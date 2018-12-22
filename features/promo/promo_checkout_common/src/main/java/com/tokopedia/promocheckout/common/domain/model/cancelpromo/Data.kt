package com.tokopedia.promocheckout.common.domain.model.cancelpromo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("success")
    @Expose
    var isSuccess: Boolean = false

}

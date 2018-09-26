package com.tokopedia.payment.setting.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseDeleteCC {

    @SerializedName("data")
    @Expose
    var data: DataResponseDeleteCC? = null

}

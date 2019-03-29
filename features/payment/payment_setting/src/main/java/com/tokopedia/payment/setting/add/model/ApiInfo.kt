package com.tokopedia.payment.setting.add.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiInfo {

    @SerializedName("host")
    @Expose
    var host: String? = null
    @SerializedName("method")
    @Expose
    var method: String? = null
    @SerializedName("headers")
    @Expose
    var headers: Headers? = null

}

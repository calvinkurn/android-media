package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePhoneData {

    @SerializedName("data")
    @Expose
    var data: PhoneDataEntity? = null

    @SerializedName("code")
    @Expose
    var code: Int = 0

    @SerializedName("latency")
    @Expose
    var latency: String? = null

    override fun toString(): String {
        return "ResponsePhoneData{" +
                "data=" + data +
                ", code=" + code +
                ", latency=" + latency +
                '}'.toString()
    }
}

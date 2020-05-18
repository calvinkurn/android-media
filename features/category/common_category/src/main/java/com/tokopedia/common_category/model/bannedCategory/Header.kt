package com.tokopedia.common_category.model.bannedCategory

import com.google.gson.annotations.SerializedName

class Header {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("server_prosess_time")
    var serverProsessTime: String? = null

    override fun toString(): String {
        return "Header{" +
                "code = '" + code + '\''.toString() +
                ",server_prosess_time = '" + serverProsessTime + '\''.toString() +
                "}"
    }
}
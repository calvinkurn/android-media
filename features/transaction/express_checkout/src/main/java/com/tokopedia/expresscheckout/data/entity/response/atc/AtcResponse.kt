package com.tokopedia.expresscheckout.data.entity.response.atc

import com.google.gson.annotations.SerializedName
import com.tokopedia.expresscheckout.data.entity.response.Header

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

data class AtcResponse(
        @SerializedName("header")
        val header: Header,

        @SerializedName("data")
        val data: AtcData,

        @SerializedName("status")
        val status: String,

        @SerializedName("error_message")
        val errorMessage: ArrayList<String>
)
package com.tokopedia.expresscheckout.data.entity.atc

import com.google.gson.annotations.SerializedName
import com.tokopedia.expresscheckout.data.entity.Header

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

data class AtcResponse(
        @SerializedName("header")
        val header: Header,

        @SerializedName("data")
        val data: AtcData,

        @SerializedName("status")
        val status: String
)
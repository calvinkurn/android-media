package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-01.
 */

data class FreeOngkir(
        @SerializedName("ticker_message")
        val tickerMessage: String = "",

        @SerializedName("error_code")
        val errorCode: String = "",

        @SerializedName("error_message")
        val errorMessage: String = ""
)
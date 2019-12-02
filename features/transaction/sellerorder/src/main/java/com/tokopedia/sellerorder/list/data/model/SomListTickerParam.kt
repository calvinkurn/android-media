package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-28.
 */

data class SomListTickerParam(
        @SerializedName("request_by")
        @Expose
        var requestBy: String = "",

        @SerializedName("client")
        @Expose
        var client: String = ""
)
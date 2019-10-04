package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-28.
 */

data class SomDetailParam(
        @SerializedName("orderID")
        @Expose
        var orderId: String = "",

        @SerializedName("lang")
        @Expose
        var lang: String = ""
)
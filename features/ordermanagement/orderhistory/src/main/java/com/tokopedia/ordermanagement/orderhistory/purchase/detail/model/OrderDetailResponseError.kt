package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by kris on 11/23/17. Tokopedia
 */
data class OrderDetailResponseError(
        @SerializedName("title")
        @Expose
        var title: String? = null,

        @SerializedName("detail")
        @Expose
        var detail: String? = null
)

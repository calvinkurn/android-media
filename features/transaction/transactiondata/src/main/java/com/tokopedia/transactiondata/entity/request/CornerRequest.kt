package com.tokopedia.transactiondata.entity.request

import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 2019-06-12.
 */
data class CornerRequest(
        @SerializedName("search_key")
        var searchKey: String = "",
        @SerializedName("show_address")
        val showAddress: Boolean,
        @SerializedName("show_corner")
        val showCorner: Boolean
)
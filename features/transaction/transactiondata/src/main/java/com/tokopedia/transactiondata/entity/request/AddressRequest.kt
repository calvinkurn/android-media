package com.tokopedia.transactiondata.entity.request
import com.google.gson.annotations.SerializedName


/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressRequest(
    @SerializedName("limit")
    var limit: Int = 10,
    @SerializedName("page")
    var page: Int = 1,
    @SerializedName("search_key")
    var searchKey: String = "",
    @SerializedName("show_address")
    val showAddress: Boolean,
    @SerializedName("show_corner")
    val showCorner: Boolean
)
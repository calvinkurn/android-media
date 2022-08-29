package com.tokopedia.manageaddress.domain.model.shareaddress

import com.google.gson.annotations.SerializedName

data class GetSharedAddressListParam(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("search_key")
    val searchKey: String
)
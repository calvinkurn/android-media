package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
//TODO to be changed
data class DataResponse(
        @SerializedName("data")
        val data: DiscoveryResponse)
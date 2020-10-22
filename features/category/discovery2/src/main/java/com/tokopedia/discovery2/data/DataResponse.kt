package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class DataResponse(
        @SerializedName("data")
        val data: DiscoveryResponse)
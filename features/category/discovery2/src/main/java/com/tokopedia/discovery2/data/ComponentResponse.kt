package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class ComponentResponse(
        @SerializedName("data")
        val data: DiscoveryResponse)
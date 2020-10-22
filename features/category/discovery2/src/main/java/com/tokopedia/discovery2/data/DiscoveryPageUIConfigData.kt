package com.tokopedia.discovery2.data


import com.google.gson.annotations.SerializedName

data class DiscoveryPageUIConfigData(
        @SerializedName("discoveryPageUIConfig")
        val discoveryPageUIConfig: DiscoveryPageUIConfig?
) {
    data class DiscoveryPageUIConfig(
            @SerializedName("data")
            val `data`: Data?
    ) {
        data class Data(
                @SerializedName("config")
                val config: String?
        )
    }
}
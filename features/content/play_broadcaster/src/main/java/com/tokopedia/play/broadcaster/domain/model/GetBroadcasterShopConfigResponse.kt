package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 26/05/20.
 */
data class GetBroadcasterShopConfigResponse(
        @SerializedName("broadcasterGetShopConfig")
        val config: GetBroadcasterShopConfig
) {
        data class GetBroadcasterShopConfigData(
                @SerializedName("data")
                val data: GetBroadcasterShopConfigResponse
        )

        data class GetBroadcasterShopConfig(
                @SerializedName("streamAllowed")
                val streamAllowed: Boolean = false,
                @SerializedName("config")
                val config: Config = Config()
        )

        data class Config(
                @SerializedName("IsLiveActive")
                val isLiveActive: Boolean = false,
                @SerializedName("ActiveLiveChannel")
                val activeLiveChannel: Int = 0,
                @SerializedName("DraftChannel")
                val draftChannel: Int = 0,
                @SerializedName("MaxDuration")
                val maxDuration: Long = 0,
                @SerializedName("MaxDurationDesc")
                val maxDurationDesc: String = "",
                @SerializedName("MaxTaggedProduct")
                val maxTaggedProduct: Int = 0,
                @SerializedName("MinTaggedProduct")
                val minTaggedProduct: Int = 0,
                @SerializedName("MaxTaggedProductDesc")
                val maxTaggedProductDesc: String = "",
                @SerializedName("MaxPauseDuration")
                val maxPauseDuration: Long = 0
        )
}
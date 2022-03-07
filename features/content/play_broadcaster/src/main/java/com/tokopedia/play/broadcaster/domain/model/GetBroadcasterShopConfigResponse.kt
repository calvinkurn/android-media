package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 26/05/20.
 */
data class GetBroadcasterShopConfigResponse(
        @SerializedName("broadcasterGetShopConfig")
        val shopConfig: GetBroadcasterShopConfig = GetBroadcasterShopConfig()
) {
        data class GetBroadcasterShopConfig(
                @SerializedName("streamAllowed")
                val streamAllowed: Boolean = false,
                @SerializedName("config")
                val config: String = "",
                @SerializedName("tnc")
                val tnc: List<TermsAndCondition> = emptyList(),
        )

        data class TermsAndCondition(
                @SerializedName("description")
                val description: String = "",
        )
}
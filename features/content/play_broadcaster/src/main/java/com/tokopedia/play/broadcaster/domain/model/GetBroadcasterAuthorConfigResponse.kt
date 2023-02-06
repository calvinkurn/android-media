package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 26/05/20.
 */
data class GetBroadcasterAuthorConfigResponse(
        @SerializedName("broadcasterGetAuthorConfig")
        val authorConfig: GetBroadcasterAuthorConfig = GetBroadcasterAuthorConfig()
) {
        data class GetBroadcasterAuthorConfig(
                @SerializedName("streamAllowed")
                val streamAllowed: Boolean = false,
                @SerializedName("shortVideoAllowed")
                val shortVideoAllowed: Boolean = false,
                @SerializedName("isBanned")
                val isBanned: Boolean = false,
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

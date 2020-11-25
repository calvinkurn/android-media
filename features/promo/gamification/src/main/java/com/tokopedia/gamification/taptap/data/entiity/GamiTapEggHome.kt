package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName
import java.util.*

data class GamiTapEggHome(
        @SerializedName("actionButton")
        val actionButton: ArrayList<ActionButton>? = null,

        @SerializedName("backButton")
        val backButton: BackButton? = null,

        @SerializedName("rewardButton")
        val rewardButton: List<RewardButton>? = null,

        @SerializedName("timeRemaining")
        val timeRemaining: TimeRemaining? = null,

        @SerializedName("tokenAsset")
        val tokenAsset: TokenAsset? = null,

        @SerializedName("tokensUser")
        val tokensUser: TokensUser? = null
)
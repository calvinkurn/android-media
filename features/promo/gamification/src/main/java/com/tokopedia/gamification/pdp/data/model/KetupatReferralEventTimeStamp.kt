package com.tokopedia.gamification.pdp.data.model

import com.google.gson.annotations.SerializedName

data class KetupatReferralEventTimeStamp(
    @SerializedName("gameReferralStamp") var gameReferralStamp: GameReferralStamp?,
    @SerializedName("gameReferralEventContent") var gameReferralEventContent: GameReferralEventContent?
) {
    data class GameReferralEventContent(
        @SerializedName("resultStatus") var resultStatus: ResultStatus?,
        @SerializedName("eventContent") var eventContent: EventContent?
    )

    data class EventContent(
        @SerializedName("remainingTime") var remainingTime: Int
    )

    data class ResultStatus(
        @SerializedName("code") var code: String?,
        @SerializedName("message") var message: ArrayList<String>,
        @SerializedName("reason") var reason: String?
    )

    data class GameReferralStamp(

        @SerializedName("resultStatus") var resultStatus: ResultStatus?,
        @SerializedName("currentStampCount") var currentStampCount: Int?,
        @SerializedName("maxStampCount") var maxStampCount: Int?,
        @SerializedName("stampLevelData") var stampLevelData: ArrayList<StampLevelData>

    )

    data class StampLevelData(

        @SerializedName("LevelIndex") var LevelIndex: Int,
        @SerializedName("TotalStampNeeded") var TotalStampNeeded: Int

    )
}

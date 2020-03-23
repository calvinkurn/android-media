package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName

data class RemindMeEntity(@SerializedName("gameRemindMe") val gameRemindMe: GameRemindMe)
data class RemindMeCheckEntity(@SerializedName("gameRemindMeCheck") val gameRemindMeCheck: GameRemindMeCheck)

data class GameRemindMe(
        @SerializedName("resultStatus") val resultStatus: ResultStatus
)

data class GameRemindMeCheck(
        @SerializedName("resultStatus") val resultStatus: ResultStatus,
        @SerializedName("isRemindMe") val isRemindMe: Boolean
)

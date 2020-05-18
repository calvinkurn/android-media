package com.tokopedia.gamification.giftbox.data.entities

import com.google.gson.annotations.SerializedName

data class AutoApplyResponse(
        @SerializedName("tokopointsSetAutoApply")
        val tokopointsSetAutoApply: TokopointsSetAutoApply? = null
)

data class TokopointsSetAutoApply(

        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null
)


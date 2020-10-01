package com.tokopedia.promotionstarget.data.autoApply

import com.google.gson.annotations.SerializedName
import com.tokopedia.promotionstarget.data.claim.ResultStatus

data class AutoApplyResponse(
        @SerializedName("tokopointsSetAutoApply")
        val tokopointsSetAutoApply: ResultContainer? = null
)

data class ResultContainer(

        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null
)

data class UpdateGratificationNotificationResponse(
        @SerializedName("updateNotification")
        val result: ResultContainer? = null
)


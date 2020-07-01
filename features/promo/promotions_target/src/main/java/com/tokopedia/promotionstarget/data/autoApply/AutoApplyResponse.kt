package com.tokopedia.promotionstarget.data.autoApply

import com.google.gson.annotations.SerializedName
import com.tokopedia.promotionstarget.data.claim.ResultStatus

data class AutoApplyResponse(
        @SerializedName("tokopointsSetAutoApply")
        val tokopointsSetAutoApply: TokopointsSetAutoApply? = null
)

data class TokopointsSetAutoApply(

        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null
)


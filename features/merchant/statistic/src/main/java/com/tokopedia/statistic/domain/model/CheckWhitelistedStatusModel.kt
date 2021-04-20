package com.tokopedia.statistic.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 17/02/21
 */

data class CheckWhitelistedStatusResponse(
        @SerializedName("checkWhitelistedStatus")
        val whitelistedStatus: CheckWhitelistedStatusModel
)

data class CheckWhitelistedStatusModel(
        @SerializedName("Whitelisted")
        val isWhitelisted: Boolean = false
)
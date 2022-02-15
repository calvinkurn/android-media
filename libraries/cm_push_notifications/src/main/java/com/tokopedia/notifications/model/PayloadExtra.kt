package com.tokopedia.notifications.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.PayloadExtraDataKey

data class PayloadExtra(
    @SerializedName(PayloadExtraDataKey.SESSION_ID)
    val sessionId : String? = null,

    @SerializedName(PayloadExtraDataKey.CAMPAIGN_NAME)
    val campaignName : String? = null,

    @SerializedName(PayloadExtraDataKey.JOURNEY_ID)
    val journeyId : String? = null,

    @SerializedName(PayloadExtraDataKey.JOURNEY_NAME)
    val journeyName : String? = null,

)

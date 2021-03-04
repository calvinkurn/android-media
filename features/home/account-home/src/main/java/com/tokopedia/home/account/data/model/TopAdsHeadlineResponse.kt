package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class TopAdsHeadlineResponse(
        @SerializedName("displayAdsV3") val displayAds: CpmModel = CpmModel()
)
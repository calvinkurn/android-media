package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class CpmTopAdsResponse(

        @SerializedName("displayAdsV3")
        val cpmModelData: CpmModel? = null
)
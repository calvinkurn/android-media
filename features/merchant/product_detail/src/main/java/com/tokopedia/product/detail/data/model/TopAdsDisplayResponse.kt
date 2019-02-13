package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class TopAdsDisplayResponse(
        @SerializedName("displayAdsV3")
        @Expose
        val result: TopAdsModel
)
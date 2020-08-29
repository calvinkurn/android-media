package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class ProductTopAdsModel(
        @SerializedName("productAds")
        @Expose
        val topAdsModel: TopAdsModel = TopAdsModel()
)
package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class ShopAdsModel(
    @SerializedName("headlineAds")
    @Expose
    val cpmModel: CpmModel = CpmModel()
)

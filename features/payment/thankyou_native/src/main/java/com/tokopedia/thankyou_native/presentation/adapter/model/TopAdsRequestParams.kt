package com.tokopedia.thankyou_native.presentation.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class TopAdsRequestParams(
    @SerializedName("type")
    var type: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("desc")
    var description: String,
    @SerializedName("ep")
    var adsType: String,
    @SerializedName("inventory_id")
    var inventoryId: String,
    @SerializedName("item")
    var itemCount: String,
    @SerializedName("dimen_id_mobile")
    var dimen: String,
    @SerializedName("data")
    var topAdsImageViewModel: ArrayList<TopAdsImageViewModel>?,
)
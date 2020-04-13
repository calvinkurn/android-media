package com.tokopedia.recharge_slice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeFavoriteRecommendationModel(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
)
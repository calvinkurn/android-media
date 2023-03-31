package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.SerializedName

data class RechargeGeneralDppoConsent(
    @SerializedName("digiPersoGetPersonalizedItems")
    val persoData: RechargeRecommendationData
)

data class RechargeRecommendationData(
    @SerializedName("items")
    val items: List<RechargeRecommendationItem>
)

data class RechargeRecommendationItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = ""
)

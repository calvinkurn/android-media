package com.tokopedia.common_digital.common.presentation.model

import com.google.gson.annotations.SerializedName

data class DigitalDppoConsent(
    @SerializedName("digiPersoGetPersonalizedItems")
    val persoData: DigiPersoRecommendationData
)

data class DigiPersoRecommendationData(
    @SerializedName("items")
    val items: List<DigiPersoRecommendationItem>
)

data class DigiPersoRecommendationItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = ""
)

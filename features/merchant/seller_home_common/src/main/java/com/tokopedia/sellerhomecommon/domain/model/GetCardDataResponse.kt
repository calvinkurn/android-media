package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetCardDataResponse(
    @SerializedName("fetchCardWidgetData")
    val getCardData: GetCardDataModel?
)

data class GetCardDataModel(
    @SerializedName("data")
    val cardData: List<CardDataModel>?
)

data class CardDataModel(
    @SerializedName("dataKey", alternate = ["data_key"])
    val dataKey: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("descriptionSecondary")
    val secondaryDescription: String?,
    @SerializedName("errorMsg", alternate = ["error_msg"])
    val errorMsg: String?,
    @SerializedName("showWidget", alternate = ["show_widget"])
    val showWidget: Boolean?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("badgeImageUrl")
    val badgeImageUrl: String?,
)

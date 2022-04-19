package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetCardDataResponse(
        @Expose
        @SerializedName("fetchCardWidgetData")
        val getCardData: GetCardDataModel?
)

data class GetCardDataModel(
        @Expose
        @SerializedName("data")
        val cardData: List<CardDataModel>?
)

data class CardDataModel(
        @Expose
        @SerializedName("dataKey")
        val dataKey: String?,
        @Expose
        @SerializedName("description")
        val description: String?,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String?,
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean?,
        @Expose
        @SerializedName("state")
        val state: String?,
        @Expose
        @SerializedName("value")
        val value: String?
)
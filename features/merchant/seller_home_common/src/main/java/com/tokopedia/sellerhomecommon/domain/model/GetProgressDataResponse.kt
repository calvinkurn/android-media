package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetProgressDataResponse(
        @Expose
        @SerializedName("fetchProgressBarWidgetData")
        val getProgressBarData: ProgressDataWrapper?
)

data class ProgressDataWrapper(
        @Expose
        @SerializedName("data")
        val progressData: List<ProgressDataModel>?
)

data class ProgressDataModel(
        @Expose
        @SerializedName("dataKey")
        val dataKey: String?,

        @Expose
        @SerializedName("valueTxt")
        val valueText: String?,

        @Expose
        @SerializedName("maxValueTxt")
        val maxValueText: String?,

        @Expose
        @SerializedName("value")
        val value: Int?,

        @Expose
        @SerializedName("maxValue")
        val maxValue: Int?,

        @Expose
        @SerializedName("state")
        val state: String?,

        @Expose
        @SerializedName("subtitle")
        val subtitle: String?,

        @Expose
        @SerializedName("errorMessage")
        val error: Boolean?,

        @Expose
        @SerializedName("errorMsg")
        val errorMessage: String?,

        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean?
)
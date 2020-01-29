package com.tokopedia.sellerhome.view.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget

data class ProgressDataUiModel(
        @SerializedName("barTitle")
        val barTitle: String,
        @SerializedName("valueTxt")
        val valueTxt: String,
        @SerializedName("maxValuetext")
        val maxValueTxt: String,
        @SerializedName("value")
        val value: Int,
        @SerializedName("max_value")
        val maxValue: Int,
        @SerializedName("colorState")
        val colorState: ShopScorePMWidget.State,
        @SerializedName("subtitle")
        val subtitle: String
)
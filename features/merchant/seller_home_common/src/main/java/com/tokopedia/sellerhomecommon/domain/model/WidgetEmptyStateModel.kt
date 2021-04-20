package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomecommon.presentation.model.WidgetEmptyStateUiModel

data class WidgetEmptyStateModel(
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("description")
        val description: String = "",
        @Expose
        @SerializedName("ctaText")
        val ctaText: String = "",
        @Expose
        @SerializedName("applink")
        val appLink: String = ""
) {
        fun mapToUiModel() = WidgetEmptyStateUiModel(imageUrl, title, description, ctaText, appLink)
}
package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget

data class SearchInspirationWidgetModel(
        @SerializedName("searchInspirationWidget")
        @Expose
        val searchInspirationWidget: SearchInspirationWidget = SearchInspirationWidget()
)
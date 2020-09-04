package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.domain.model.SearchProductModel.GlobalSearchNavigation

data class GlobalSearchNavigationModel(
        @SerializedName("global_search_navigation")
        @Expose
        val globalSearchNavigation: GlobalSearchNavigation = GlobalSearchNavigation()
)
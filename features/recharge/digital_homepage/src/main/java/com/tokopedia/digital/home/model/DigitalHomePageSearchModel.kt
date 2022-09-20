package com.tokopedia.digital.home.model

import com.tokopedia.digital.home.presentation.model.DigitalHomePageSearchCategoryModel

data class DigitalHomePageSearchNewModel(
    val isFromAutoComplete: Boolean = false,
    val trackerUser: Tracking = Tracking(),
    val searchQuery: String = "",
    val listSearchResult: List<DigitalHomePageSearchCategoryModel> = emptyList()
)
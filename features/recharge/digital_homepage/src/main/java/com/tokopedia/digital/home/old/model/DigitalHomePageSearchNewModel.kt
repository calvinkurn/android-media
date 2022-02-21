package com.tokopedia.digital.home.old.model

import com.tokopedia.digital.home.model.Tracking

data class DigitalHomePageSearchNewModel (
       val isFromAutoComplete: Boolean = false,
       val trackerUser: Tracking = Tracking(),
       val searchQuery: String = "",
       val listSearchResult: List<DigitalHomePageSearchCategoryModel> = emptyList()
)
package com.tokopedia.tokopedianow.category.presentation.model

import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

data class CategoryEmptyStateModel(
    val queryParams: MutableMap<String, String>,
    val violation: AceSearchProductModel.Violation,
    val excludedFilter: Option?,
    val shouldShow: Boolean
)

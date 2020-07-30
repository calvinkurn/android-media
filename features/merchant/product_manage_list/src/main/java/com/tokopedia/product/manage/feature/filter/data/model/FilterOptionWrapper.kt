package com.tokopedia.product.manage.feature.filter.data.model

import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption

data class FilterOptionWrapper (
        val sortOption: SortOption? = null,
        val filterOptions: List<FilterOption> = listOf(),
        val filterShownState: List<Boolean> = listOf(),
        val selectedFilterCount: Int = 0
)
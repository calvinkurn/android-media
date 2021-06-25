package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.filter.common.data.Option

data class CategoryFilterItemDataView(
     val option: Option = Option(),
     val isSelected: Boolean = false,
)
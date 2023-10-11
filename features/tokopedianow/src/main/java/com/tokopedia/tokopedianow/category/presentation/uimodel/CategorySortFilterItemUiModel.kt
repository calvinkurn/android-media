package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.unifycomponents.ChipsUnify

data class CategorySortFilterItemUiModel(
    val filter: Filter = Filter(),
    val chipType: String = ChipsUnify.TYPE_NORMAL,
    val showNewNotification: Boolean = false
)

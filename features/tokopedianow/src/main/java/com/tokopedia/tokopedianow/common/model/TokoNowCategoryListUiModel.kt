package com.tokopedia.tokopedianow.common.model

data class TokoNowCategoryListUiModel (
    val categoryList: List<TokoNowCategoryItemUiModel>,
    val gridSpanCount: Int = 1
)
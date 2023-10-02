package com.tokopedia.tokopedianow.category.presentation.model

import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse.CategoryDetail
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData

data class CategoryL2TabData(
    val title: String = "",
    val categoryIdL1: String = "",
    val categoryIdL2: String = "",
    val tickerData: GetTickerData = GetTickerData(),
    val componentList: List<Component> = emptyList(),
    val categoryDetail: CategoryDetail = CategoryDetail()
)

package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse.CategoryDetail
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class CategoryL2TabUiModel(
    val id: String = "",
    val tabList: List<CategoryL2TabData> = emptyList(),
    val categoryDetail: CategoryDetail = CategoryDetail(),
    var selectedTabPosition: Int = 0,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
)

package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

object HomeCategoryMapper {

    private const val MAX_HOME_CATEGORY_ITEM_COUNT = 8

    fun mapToCategoryLayout(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val categoryGridUiModel = TokoNowCategoryGridUiModel(response.id, response.header.name, emptyList(), TokoNowLayoutState.LOADING)
        return HomeLayoutItemUiModel(categoryGridUiModel, state)
    }

    fun mapToCategoryList(response: List<CategoryResponse>?): List<TokoNowCategoryItemUiModel>? {
        return response?.take(MAX_HOME_CATEGORY_ITEM_COUNT)?.map {
            TokoNowCategoryItemUiModel(it.id, it.name, it.imageUrl, it.appLinks)
        }
    }
}
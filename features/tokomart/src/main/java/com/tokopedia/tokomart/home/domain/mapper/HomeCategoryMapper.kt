package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.home.constant.HomeLayoutItemState
import com.tokopedia.tokomart.home.constant.HomeLayoutState
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryItemUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutItemUiModel

object HomeCategoryMapper {

    private const val MAX_HOME_CATEGORY_ITEM_COUNT = 8

    fun mapToCategoryLayout(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val categoryGridUiModel = HomeCategoryGridUiModel(response.id, response.header.name, emptyList(), HomeLayoutState.LOADING)
        return HomeLayoutItemUiModel(categoryGridUiModel, state)
    }

    fun mapToCategoryList(response: List<CategoryResponse>?): List<HomeCategoryItemUiModel>? {
        return response?.take(MAX_HOME_CATEGORY_ITEM_COUNT)?.map {
            HomeCategoryItemUiModel(it.id, it.name, it.imageUrl, it.appLinks)
        }
    }
}
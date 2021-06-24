package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryItemUiModel

object HomeCategoryMapper {

    private const val MAX_HOME_CATEGORY_ITEM_COUNT = 8

    fun mapToCategoryLayout(response: HomeLayoutResponse): HomeCategoryGridUiModel {
        return HomeCategoryGridUiModel(response.id, response.header.name, emptyList(), HomeLayoutState.LOADING)
    }

    fun mapToCategoryList(response: List<CategoryResponse>?): List<HomeCategoryItemUiModel>? {
        return response?.take(MAX_HOME_CATEGORY_ITEM_COUNT)?.map {
            HomeCategoryItemUiModel(it.id, it.name, it.imageUrl, it.appLinks)
        }
    }
}
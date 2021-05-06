package com.tokopedia.tokomart.categorylist.domain.mapper

import com.tokopedia.tokomart.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel

object CategoryListMapper {

    fun mapToUiModel(response: List<CategoryListResponse>): List<CategoryListItemUiModel> {
        return response.map {
            val childList = mapToChildUiModel(it.childList.orEmpty())
            CategoryListItemUiModel(it.id, it.title, it.iconUrl, childList)
        }
    }

    private fun mapToChildUiModel(response: List<CategoryListResponse>): List<CategoryListChildUiModel> {
        return response.map { CategoryListChildUiModel(it.id, it.title, it.iconUrl) }
    }
}

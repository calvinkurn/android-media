package com.tokopedia.tokopedianow.categoryfilter.domain.mapper

import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.unifycomponents.ChipsUnify

object CategoryFilterMapper {

    fun mapToCategoryList(
        response: GetCategoryListResponse.CategoryListResponse,
        selectedFilterIds: List<String>
    ): List<CategoryFilterChip> {
        return response.data.map {
            val categoryChild = mapCategoryChild(it.childList.orEmpty(), selectedFilterIds)
            CategoryFilterChip(id = it.id, title = it.name, childList = categoryChild)
        }
    }

    private fun mapCategoryChild(
        categoryList: List<GetCategoryListResponse.CategoryListResponse.CategoryResponse>,
        selectedFilterIds: List<String>
    ): List<CategoryFilterChip> {
        return categoryList.map {
            val filterId = it.id
            val isSelected = selectedFilterIds.contains(filterId)

            val chipType = if(isSelected) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }

            CategoryFilterChip(it.id, it.name, chipType)
        }
    }
}

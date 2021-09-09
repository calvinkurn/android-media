package com.tokopedia.tokopedianow.categoryfilter.domain.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.unifycomponents.ChipsUnify

object CategoryFilterMapper {

    fun mapToCategoryList(
        response: CategoryListResponse,
        selectedFilterIds: List<Int>
    ): List<CategoryFilterChip> {
        return response.data.map {
            val categoryChild = mapCategoryChild(it.childList.orEmpty(), selectedFilterIds)
            CategoryFilterChip(id = it.id, title = it.name, childList = categoryChild)
        }
    }

    private fun mapCategoryChild(
        categoryList: List<CategoryResponse>,
        selectedFilterIds: List<Int>
    ): List<CategoryFilterChip> {
        return categoryList.map {
            val filterId = it.id.toIntOrZero()
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

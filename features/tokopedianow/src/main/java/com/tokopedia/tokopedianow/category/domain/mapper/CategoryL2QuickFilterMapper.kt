package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategorySortFilterItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.unifycomponents.ChipsUnify

object CategoryL2QuickFilterMapper {

    fun mapQuickFilter(
        quickFilterUiModel: CategoryQuickFilterUiModel,
        quickFilterResponse: DynamicFilterModel,
        categoryFilterResponse: DynamicFilterModel,
        filterController: FilterController
    ): CategoryQuickFilterUiModel {
        val quickFilterListResponse = quickFilterResponse.data.filter
        val categoryFilterListResponse = categoryFilterResponse.data.filter

        val categoryFilterDataValue = DataValue(
            filter = FilterHelper.copyFilterWithOptionAsExclude(categoryFilterListResponse)
        )

        val quickFilterDataValue = DataValue(
            filter = quickFilterListResponse.map { filter ->
                filter.clone(options = createOptionListWithExclude(categoryFilterDataValue, filter))
            }
        )

        val filterItemList = quickFilterDataValue.filter.map {
            val isSelected = getQuickFilterIsSelected(it, filterController)
            val chipType = getSortFilterItemType(isSelected)
            val showNewNotification = it.options.firstOrNull()?.isNew ?: false
            CategorySortFilterItemUiModel(it, chipType, showNewNotification)
        }

        return quickFilterUiModel.copy(
            itemList = filterItemList,
            state = TokoNowLayoutState.LOADED
        )
    }

    private fun createOptionListWithExclude(categoryFilterDataValue: DataValue, filter: Filter) =
        filter.options.map { option ->
            option.clone().also { copyOption ->
                modifyOptionKeyInCategoryFilter(categoryFilterDataValue, copyOption)
            }
        }

    private fun modifyOptionKeyInCategoryFilter(categoryFilterDataValue: DataValue, option: Option) {
        val isCategoryFilter = isInCategoryFilter(categoryFilterDataValue, option)

        if (isCategoryFilter) {
            option.key = OptionHelper.EXCLUDE_PREFIX + option.key
        }
    }

    private fun isInCategoryFilter(categoryFilterDataValue: DataValue, optionToCheck: Option): Boolean {
        val categoryOptionList = categoryFilterDataValue.filter.map { it.options }.flatten()

        return categoryOptionList.any {
            it.key.removePrefix(OptionHelper.EXCLUDE_PREFIX) == optionToCheck.key &&
                it.value == optionToCheck.value
        }
    }

    private fun getQuickFilterIsSelected(
        filter: Filter,
        filterController: FilterController
    ) =
        filter.options.any {
            if (it.key.contains(OptionHelper.EXCLUDE_PREFIX)) {
                false
            } else {
                filterController.getFilterViewState(it)
            }
        }

    private fun getSortFilterItemType(isSelected: Boolean) =
        if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
}

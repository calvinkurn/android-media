package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel

fun getSortDataModel(id: String = "312", isSelected: Boolean = false, sort: String = "DESC"): FilterDataUiModel {
    return FilterDataUiModel(id, "Some Sort", sort, isSelected)
}

fun getSortFilterViewModel(sortDataModel: FilterDataUiModel = getSortDataModel(), isChipsShown: Boolean = false): FilterUiModel {
    return FilterUiModel(ProductManageFilterMapper.SORT_HEADER,
            mutableListOf(
                    sortDataModel,
                    FilterDataUiModel("706", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("707", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("708", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("709", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("710", "Some Other Sort", "ASC", false)
            ), isChipsShown)
}

fun getEtalaseDataModel(id: String = "342", isSelected: Boolean = false, sort: String = ""): FilterDataUiModel {
    return FilterDataUiModel(id, "Some Etalase", sort, isSelected)
}

fun getEtalaseFilterViewModel(etalaseDataModel: FilterDataUiModel = getEtalaseDataModel(), isChipsShown: Boolean = false): FilterUiModel {
    return FilterUiModel(ProductManageFilterMapper.ETALASE_HEADER,
            mutableListOf(
                    etalaseDataModel,
                    FilterDataUiModel("123", "Some Other Etalase", "", false),
                    FilterDataUiModel("124", "Some Other Etalase", "", false),
                    FilterDataUiModel("125", "Some Other Etalase", "", false),
                    FilterDataUiModel("126", "Some Other Etalase", "", false),
                    FilterDataUiModel("127", "Some Other Etalase", "", false)
            ), isChipsShown)
}

fun getCategoryDataModel(id: String = "293"): FilterDataUiModel {
    return FilterDataUiModel(id, "Some Category", "", false)
}

fun getCategoryFilterViewModel(categoryDataModel: FilterDataUiModel = getCategoryDataModel()): FilterUiModel {
    return FilterUiModel(ProductManageFilterMapper.CATEGORY_HEADER,
            mutableListOf(
                    categoryDataModel,
                    FilterDataUiModel("452", "Some Other Category", "", false),
                    FilterDataUiModel("453", "Some Other Category", "", false),
                    FilterDataUiModel("454", "Some Other Category", "", false),
                    FilterDataUiModel("455", "Some Other Category", "", false),
                    FilterDataUiModel("456", "Some Other Category", "", false),
                    FilterDataUiModel("457", "Some Other Category", "", false),
                    FilterDataUiModel("458", "Some Other Category", "", false)
            ), false)
}

fun getOtherFilterDataModel(id: String = "4183"): FilterDataUiModel {
    return FilterDataUiModel(id, "Some Other Filter", "", false)
}

fun getOtherFilterFilterViewModel(otherFilterDataModel: FilterDataUiModel = getOtherFilterDataModel()): FilterUiModel {
    return FilterUiModel(ProductManageFilterMapper.OTHER_FILTER_HEADER,
            mutableListOf(
                    otherFilterDataModel,
                    FilterDataUiModel("611", "Some Other Filter", "", false),
                    FilterDataUiModel("612", "Some Other Filter", "", false),
                    FilterDataUiModel("613", "Some Other Filter", "", false),
                    FilterDataUiModel("614", "Some Other Filter", "", false),
                    FilterDataUiModel("615", "Some Other Filter", "", false),
                    FilterDataUiModel("616", "Some Other Filter", "", false),
                    FilterDataUiModel("617", "Some Other Filter", "", false)
            ), false)
}

fun getSelectedSortData(sortDataModel: FilterDataUiModel = getSortDataModel()): FilterUiModel {
    return FilterUiModel(ProductManageFilterMapper.SORT_HEADER,
            mutableListOf(
                    sortDataModel,
                    FilterDataUiModel("706", "Some Other Sort", "ASC", true),
                    FilterDataUiModel("707", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("708", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("709", "Some Other Sort", "ASC", false),
                    FilterDataUiModel("710", "Some Other Sort", "ASC", false)
            ), false)
}

fun getSelectedEtalaseData(etalaseDataModel: FilterDataUiModel = getEtalaseDataModel()): FilterUiModel {
    return FilterUiModel(ProductManageFilterMapper.ETALASE_HEADER,
            mutableListOf(
                    etalaseDataModel,
                    FilterDataUiModel("123", "Some Other Etalase", "", true),
                    FilterDataUiModel("124", "Some Other Etalase", "", false),
                    FilterDataUiModel("125", "Some Other Etalase", "", false),
                    FilterDataUiModel("126", "Some Other Etalase", "", false),
                    FilterDataUiModel("127", "Some Other Etalase", "", false)
            ), false)
}

fun getInitialChecklistData(): List<ChecklistUiModel> {
    return listOf(
            ChecklistUiModel(id ="1", name = "Some Filter", value = "", isSelected = false),
            ChecklistUiModel(id ="2", name = "Some Category", value = "", isSelected = true),
            ChecklistUiModel(id ="3", name = "Some Other Filter", value = "", isSelected = true),
            ChecklistUiModel(id ="4", name = "Some Other Category", value = "", isSelected = false)
    )
}

fun getUnselectedData(): List<SelectUiModel>{
    return listOf(SelectUiModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
            SelectUiModel(id = "2", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "3", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "4", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "5", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "6", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = false))
}

fun getSelectedData(): List<SelectUiModel>{
    return listOf(SelectUiModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
            SelectUiModel(id = "2", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "3", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "4", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "5", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "6", name = "Some Etalase", value = "", isSelected = false),
            SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = true))
}
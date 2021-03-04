package com.tokopedia.product.manage.feature.filter.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOrderOption
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

class ProductManageFilterMapper {

    companion object {
        const val SORT_HEADER = "Urutkan"
        const val ETALASE_HEADER = "Etalase"
        const val CATEGORY_HEADER = "Kategori"
        const val OTHER_FILTER_HEADER = "Filter Lainnya"
        private const val SHOW_CHIPS = true
        private const val HIDE_CHIPS = false

        fun mapCombinedResultToFilterViewModels(filterOptionsResponse: FilterOptionsResponse): List<FilterUiModel> {
            val filterViewModels = mutableListOf<FilterUiModel>()
            val metaData = filterOptionsResponse.productListMetaResponse.productListMetaWrapper.productListMetaData
            filterViewModels.add(mapMetaResponseToSortOptions(metaData))
            filterViewModels.add(mapEtalaseResponseToEtalaseOptions(filterOptionsResponse.shopEtalase))
            filterViewModels.add(mapCategoryResponseToCategoryOptions(filterOptionsResponse.categoriesResponse))
            filterViewModels.add(mapMetaResponseToFilterOptions(metaData))
            return filterViewModels
        }

        fun mapFilterViewModelsToSelectViewModels(filterUiModel: FilterUiModel): List<SelectUiModel> {
            val selectViewModels = mutableListOf<SelectUiModel>()
            filterUiModel.data.forEach {
                selectViewModels.add(SelectUiModel(it.id, it.name, it.value, it.select))
            }
            return selectViewModels
        }

        fun mapFilterViewModelsToChecklistViewModels(filterUiModel: FilterUiModel): List<ChecklistUiModel> {
            val checklistViewModels = mutableListOf<ChecklistUiModel>()
            filterUiModel.data.forEach {
                checklistViewModels.add(ChecklistUiModel(it.id, it.name, it.value, it.select))
            }
            return checklistViewModels
        }

        fun mapSelectViewModelsToFilterViewModel(key: String, selectUiModels: List<SelectUiModel>, isChipsShown: Boolean): FilterUiModel {
            val data = mutableListOf<FilterDataUiModel>()
            selectUiModels.forEach {
                data.add(FilterDataUiModel(it.id, it.name, it.value, it.isSelected))
            }
            return if(key == ProductManageFilterFragment.SORT_CACHE_MANAGER_KEY) {
                FilterUiModel(SORT_HEADER, data, isChipsShown)
            } else {
                FilterUiModel(ETALASE_HEADER, data, isChipsShown)
            }
        }

        fun mapChecklistViewModelsToFilterViewModel(key: String, checklistUiModels: List<ChecklistUiModel>, isChipsShown: Boolean): FilterUiModel {
            val data = mutableListOf<FilterDataUiModel>()
            checklistUiModels.forEach {
                data.add(FilterDataUiModel(it.id, it.name, it.value, it.isSelected))
            }
            return if(key == ProductManageFilterFragment.CATEGORIES_CACHE_MANAGER_KEY) {
                FilterUiModel(CATEGORY_HEADER, data, isChipsShown)
            } else {
                FilterUiModel(OTHER_FILTER_HEADER, data, isChipsShown)
            }
        }

        fun mapFiltersToFilterOptions(filterUiModel: List<FilterUiModel>): FilterOptionWrapper {
            var selectedFilterCount = 0
            val filterOptions = mutableListOf<FilterOption>()
            val sortOption = mapSortToSortOptions(filterUiModel[ProductManageFilterFragment.ITEM_SORT_INDEX])
            val isShown = mutableListOf<Boolean>()
            filterOptions.addAll(mapEtalaseToFilterOptions(filterUiModel[ProductManageFilterFragment.ITEM_ETALASE_INDEX]))
            filterOptions.addAll(mapCategoriesToFilterOptions(filterUiModel[ProductManageFilterFragment.ITEM_CATEGORIES_INDEX]))
            filterOptions.addAll(mapFiltersToFilterOptions(filterUiModel[ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX]))
            filterUiModel.forEach {
                if(it.isChipsShown) {
                    isShown.add(SHOW_CHIPS)
                } else {
                    isShown.add(HIDE_CHIPS)
                }
                selectedFilterCount += it.data.filter { data -> data.select }.size
            }
            return FilterOptionWrapper(sortOption, filterOptions, isShown, selectedFilterCount)
        }

        fun countSelectedFilter(filterOptions: List<FilterOption>): Int {
            var selectedFilterCount = 0
            filterOptions.forEach {
                when(it) {
                    is FilterOption.FilterByMenu -> {
                        selectedFilterCount =+ it.menuIds.count()
                    }
                    is FilterOption.FilterByCategory -> {
                        selectedFilterCount =+ it.categoryIds.count()
                    }
                    else -> selectedFilterCount++
                }
            }
            return selectedFilterCount
        }

        fun mapFilterOptionWrapperToSelectedSort(filterOptionWrapper: FilterOptionWrapper): FilterDataUiModel? {
            filterOptionWrapper.sortOption?.let {
                return FilterDataUiModel(id = it.id.name,
                        value = it.option.name,
                        select = false)
            }
            return null
        }

        fun mapFilterOptionWrapperToSelectedEtalase(filterOptionWrapper: FilterOptionWrapper): FilterDataUiModel? {
            filterOptionWrapper.filterOptions.filterIsInstance(FilterOption.FilterByMenu::class.java).forEach {
                    return FilterDataUiModel(it.menuIds.first(), select = false)
            }
            return null
        }

        fun mapFilterOptionWrapperToSelectedCategories(filterOptionWrapper: FilterOptionWrapper): List<FilterDataUiModel> {
            val selectedCategories = mutableListOf<FilterDataUiModel>()
            filterOptionWrapper.filterOptions.filterIsInstance(FilterOption.FilterByCategory::class.java).forEach {
                it.categoryIds.forEach { category ->
                    selectedCategories.add(
                            FilterDataUiModel(category, select = false)
                    )
                }
            }
            return selectedCategories
        }

        fun mapFilterOptionWrapperToSelectedOtherFilters(filterOptionWrapper: FilterOptionWrapper): List<FilterDataUiModel> {
            val selectedOtherFilters = mutableListOf<FilterDataUiModel>()
            filterOptionWrapper.filterOptions.filterIsInstance(FilterOption.FilterByCondition::class.java).forEach {
                selectedOtherFilters.add(
                        FilterDataUiModel(it.id, select = false)
                )
            }
            return selectedOtherFilters
        }

        private fun mapSortToSortOptions(filterUiModel: FilterUiModel): SortOption? {
            var selectedData: FilterDataUiModel? = null
            filterUiModel.data.forEach {
                if(it.select) {
                    selectedData = it
                }
            }
            var sortOrderOption = SortOrderOption.DESC
            selectedData?.let {
                if(it.value == SortOrderOption.ASC.name) sortOrderOption = SortOrderOption.ASC
            }
            return when(selectedData?.id) {
                SortOption.SortId.NAME.name -> SortOption.SortByName(sortOrderOption)
                SortOption.SortId.UPDATE_TIME.name -> SortOption.SortByUpdateTime(sortOrderOption)
                SortOption.SortId.SOLD.name -> SortOption.SortBySold(sortOrderOption)
                SortOption.SortId.PRICE.name -> SortOption.SortByPrice(sortOrderOption)
                SortOption.SortId.DEFAULT.name -> SortOption.SortByDefault(sortOrderOption)
                SortOption.SortId.STOCK.name -> SortOption.SortByStock(sortOrderOption)
                else -> null
            }
        }

        private fun mapEtalaseToFilterOptions(filterUiModel: FilterUiModel): List<FilterOption> {
            var selectedData: FilterDataUiModel? = null
            filterUiModel.data.forEach {
                if(it.select) {
                    selectedData = it
                }
            }
            selectedData?.let {
                return listOf(FilterOption.FilterByMenu(listOf(it.id)))
            }
            return listOf()
        }

        private fun mapCategoriesToFilterOptions(filterUiModel: FilterUiModel): List<FilterOption> {
            val selectedCategoryIds = mutableListOf<String>()
            filterUiModel.data.forEach {
                if(it.select) {
                    selectedCategoryIds.add(it.id)
                }
            }
            if(selectedCategoryIds.isNotEmpty()) {
                return listOf(FilterOption.FilterByCategory(selectedCategoryIds))
            }
            return listOf()
        }

        private fun mapFiltersToFilterOptions(filterUiModel: FilterUiModel): List<FilterOption> {
            val selectedData = mutableListOf<FilterOption>()
            filterUiModel.data.forEach {
                if(it.select) {
                    val mappedData = when(it.id) {
                        FilterOption.FilterByCondition.NewOnly.id -> FilterOption.FilterByCondition.NewOnly
                        FilterOption.FilterByCondition.UsedOnly.id -> FilterOption.FilterByCondition.UsedOnly
                        FilterOption.FilterByCondition.EmptyStockOnly.id -> FilterOption.FilterByCondition.EmptyStockOnly
                        FilterOption.FilterByCondition.VariantOnly.id -> FilterOption.FilterByCondition.VariantOnly
                        FilterOption.FilterByCondition.CashBackOnly.id -> FilterOption.FilterByCondition.CashBackOnly
                        FilterOption.FilterByCondition.WholesaleOnly.id -> FilterOption.FilterByCondition.WholesaleOnly
                        FilterOption.FilterByCondition.PreorderOnly.id -> FilterOption.FilterByCondition.PreorderOnly
                        FilterOption.FilterByCondition.CampaignOnly.id -> FilterOption.FilterByCondition.CampaignOnly
                        else -> FilterOption.FilterByCondition.FeaturedOnly
                    }
                    selectedData.add(mappedData)
                }
            }
            return selectedData
        }

        private fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): FilterUiModel {
            val data = mutableListOf<FilterDataUiModel>()
            productListMetaData.sorts.filter { it.name.isNotEmpty() &&
                    (it.id != SortOption.SortByDefault(SortOrderOption.DESC).id.name)}.forEach {
                data.add(FilterDataUiModel(it.id, it.name, it.value))
            }
            return FilterUiModel(SORT_HEADER, data, SHOW_CHIPS)
        }

        private fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): FilterUiModel {
            val data = mutableListOf<FilterDataUiModel>()
            etalaseResponse.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataUiModel(it.id,it.name))
            }
            return FilterUiModel(ETALASE_HEADER, data, SHOW_CHIPS)
        }

        private fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): FilterUiModel {
            val data = mutableListOf<FilterDataUiModel>()
            categoriesResponse.categories.categories.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataUiModel(it.id, it.name))
            }
            return FilterUiModel(CATEGORY_HEADER, data, HIDE_CHIPS)
        }

        private fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): FilterUiModel {
            val data = mutableListOf<FilterDataUiModel>()
            productListMetaData.filters.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataUiModel(it.id, it.name, it.value.toString()))
            }
            return FilterUiModel(OTHER_FILTER_HEADER, data, HIDE_CHIPS)
        }
    }
}
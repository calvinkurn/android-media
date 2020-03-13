package com.tokopedia.product.manage.feature.filter.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*
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

        fun mapCombinedResultToFilterViewModels(filterOptionsResponse: FilterOptionsResponse): List<FilterViewModel> {
            val filterViewModels = mutableListOf<FilterViewModel>()
            val metaData = filterOptionsResponse.productListMetaResponse.productListMetaWrapper.productListMetaData
            filterViewModels.add(mapMetaResponseToSortOptions(metaData))
            filterViewModels.add(mapEtalaseResponseToEtalaseOptions(filterOptionsResponse.shopEtalase))
            filterViewModels.add(mapCategoryResponseToCategoryOptions(filterOptionsResponse.categoriesResponse))
            filterViewModels.add(mapMetaResponseToFilterOptions(metaData))
            return filterViewModels
        }

        fun mapFilterViewModelsToSelectViewModels(filterViewModel: FilterViewModel): List<SelectViewModel> {
            val selectViewModels = mutableListOf<SelectViewModel>()
            filterViewModel.data.forEach {
                selectViewModels.add(SelectViewModel(it.id, it.name, it.value, it.select))
            }
            return selectViewModels
        }

        fun mapFilterViewModelsToChecklistViewModels(filterViewModel: FilterViewModel): List<ChecklistViewModel> {
            val checklistViewModels = mutableListOf<ChecklistViewModel>()
            filterViewModel.data.forEach {
                checklistViewModels.add(ChecklistViewModel(it.id, it.name, it.value, it.select))
            }
            return checklistViewModels
        }

        fun mapSelectViewModelsToFilterViewModel(key: String, selectViewModels: List<SelectViewModel>, isChipsShown: Boolean): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            selectViewModels.forEach {
                data.add(FilterDataViewModel(it.id, it.name, it.value, it.isSelected))
            }
            return if(key == ProductManageFilterFragment.SORT_CACHE_MANAGER_KEY) {
                FilterViewModel(SORT_HEADER, data, isChipsShown)
            } else {
                FilterViewModel(ETALASE_HEADER, data, isChipsShown)
            }
        }

        fun mapChecklistViewModelsToFilterViewModel(key: String, checklistViewModels: List<ChecklistViewModel>, isChipsShown: Boolean): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            checklistViewModels.forEach {
                data.add(FilterDataViewModel(it.id, it.name, it.value, it.isSelected))
            }
            return if(key == ProductManageFilterFragment.CATEGORIES_CACHE_MANAGER_KEY) {
                FilterViewModel(CATEGORY_HEADER, data, isChipsShown)
            } else {
                FilterViewModel(OTHER_FILTER_HEADER, data, isChipsShown)
            }
        }

        fun mapFiltersToFilterOptions(filterViewModel: List<FilterViewModel>): FilterOptionWrapper {
            val filterOptions = mutableListOf<FilterOption>()
            val sortOption = mapSortToSortOptions(filterViewModel[ProductManageFilterFragment.ITEM_SORT_INDEX])
            filterOptions.addAll(mapEtalaseToFilterOptions(filterViewModel[ProductManageFilterFragment.ITEM_ETALASE_INDEX]))
            filterOptions.addAll(mapCategoriesToFilterOptions(filterViewModel[ProductManageFilterFragment.ITEM_CATEGORIES_INDEX]))
            filterOptions.addAll(mapFiltersToFilterOptions(filterViewModel[ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX]))
            return FilterOptionWrapper(sortOption, filterOptions)
        }

        fun mapFilterOptionWrapperToSelectedSort(filterOptionWrapper: FilterOptionWrapper): FilterDataViewModel? {
            filterOptionWrapper.sortOption?.let {
                return FilterDataViewModel(id = it.id.name,
                        value = it.option.name,
                        select = false)
            }
            return null
        }

        fun mapFilterOptionWrapperToSelectedEtalase(filterOptionWrapper: FilterOptionWrapper): FilterDataViewModel? {
            filterOptionWrapper.filterOptions.forEach {
                if (it is FilterOption.FilterByMenu) {
                    return FilterDataViewModel(it.menuIds.first(), select = false)
                }
            }
            return null
        }

        fun mapFilterOptionWrapperToSelectedCategories(filterOptionWrapper: FilterOptionWrapper): List<FilterDataViewModel> {
            val selectedCategories = mutableListOf<FilterDataViewModel>()
            filterOptionWrapper.filterOptions.forEach {
                if (it is FilterOption.FilterByCategory) {
                    it.categoryIds.forEach { category ->
                        selectedCategories.add(
                                FilterDataViewModel(category, select = false)
                        )
                    }
                }
            }
            return selectedCategories
        }

        fun mapFilterOptionWrapperToSelectedOtherFilters(filterOptionWrapper: FilterOptionWrapper): List<FilterDataViewModel> {
            val selectedOtherFilters = mutableListOf<FilterDataViewModel>()
            filterOptionWrapper.filterOptions.forEach {
                if (it is FilterOption.FilterByCondition) {
                    selectedOtherFilters.add(
                            FilterDataViewModel(it.id, select = false)
                    )
                }
            }
            return selectedOtherFilters
        }

        private fun mapSortToSortOptions(filterViewModel: FilterViewModel): SortOption {
            var selectedData: FilterDataViewModel? = null
            filterViewModel.data.forEach {
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
                else -> SortOption.SortByDefault(sortOrderOption)
            }
        }

        private fun mapEtalaseToFilterOptions(filterViewModel: FilterViewModel): List<FilterOption> {
            var selectedData: FilterDataViewModel? = null
            filterViewModel.data.forEach {
                if(it.select) {
                    selectedData = it
                }
            }
            selectedData?.let {
                return listOf(FilterOption.FilterByMenu(listOf(it.id)))
            }
            return listOf()
        }

        private fun mapCategoriesToFilterOptions(filterViewModel: FilterViewModel): List<FilterOption> {
            val selectedCategoryIds = mutableListOf<String>()
            filterViewModel.data.forEach {
                if(it.select) {
                    selectedCategoryIds.add(it.id)
                }
            }
            if(selectedCategoryIds.isNotEmpty()) {
                return listOf(FilterOption.FilterByCategory(selectedCategoryIds))
            }
            return listOf()
        }

        private fun mapFiltersToFilterOptions(filterViewModel: FilterViewModel): List<FilterOption> {
            val selectedData = mutableListOf<FilterOption>()
            filterViewModel.data.forEach {
                if(it.select) {
                    val mappedData = when(it.id) {
                        FilterOption.FilterByCondition.NewOnly.id -> FilterOption.FilterByCondition.NewOnly
                        FilterOption.FilterByCondition.UsedOnly.id -> FilterOption.FilterByCondition.UsedOnly
                        FilterOption.FilterByCondition.EmptyStockOnly.id -> FilterOption.FilterByCondition.EmptyStockOnly
                        FilterOption.FilterByCondition.VariantOnly.id -> FilterOption.FilterByCondition.VariantOnly
                        FilterOption.FilterByCondition.CashBackOnly.id -> FilterOption.FilterByCondition.CashBackOnly
                        FilterOption.FilterByCondition.WholesaleOnly.id -> FilterOption.FilterByCondition.WholesaleOnly
                        FilterOption.FilterByCondition.PreorderOnly.id -> FilterOption.FilterByCondition.PreorderOnly
                        else -> FilterOption.FilterByCondition.FeaturedOnly
                    }
                    selectedData.add(mappedData)
                }
            }
            return selectedData
        }

        private fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            productListMetaData.sorts.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataViewModel(it.id, it.name, it.value))
            }
            return FilterViewModel(SORT_HEADER, data, SHOW_CHIPS)
        }

        private fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            etalaseResponse.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataViewModel(it.id,it.name))
            }
            return FilterViewModel(ETALASE_HEADER, data, SHOW_CHIPS)
        }

        private fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            categoriesResponse.categories.categories.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataViewModel(it.id, it.name))
            }
            return FilterViewModel(CATEGORY_HEADER, data, HIDE_CHIPS)
        }

        private fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            productListMetaData.filters.filter { it.name.isNotEmpty() }.forEach {
                data.add(FilterDataViewModel(it.id, it.name, it.value.toString()))
            }
            return FilterViewModel(OTHER_FILTER_HEADER, data, HIDE_CHIPS)
        }
    }
}
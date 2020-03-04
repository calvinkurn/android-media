package com.tokopedia.product.manage.feature.filter.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
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

        fun mapSelectViewModelsToFilterViewModel(key: String, selectViewModels: List<SelectViewModel>): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            selectViewModels.forEach {
                data.add(FilterDataViewModel(it.id, it.name, it.value, it.isSelected))
            }
            return if(key == ProductManageFilterFragment.SORT_CACHE_MANAGER_KEY) {
                FilterViewModel(SORT_HEADER, data, SHOW_CHIPS)
            } else {
                FilterViewModel(ETALASE_HEADER, data, SHOW_CHIPS)
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
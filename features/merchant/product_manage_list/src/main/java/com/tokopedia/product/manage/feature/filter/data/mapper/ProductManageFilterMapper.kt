package com.tokopedia.product.manage.feature.filter.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.CombinedResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

class ProductManageFilterMapper {

    companion object {
        const val SORT_HEADER = "Urutkan"
        const val ETALASE_HEADER = "Etalase"
        const val CATEGORY_HEADER = "Kategori"
        const val OTHER_FILTER_HEADER = "Filter Lainnya"
        const val SHOW_CHIPS = true
        const val HIDE_CHIPS = false

        fun mapCombinedResultToFilterViewModels(combinedData: CombinedResponse): List<FilterViewModel> {
            val filterViewModels = mutableListOf<FilterViewModel>()
            filterViewModels.add(mapMetaResponseToSortOptions(combinedData.productListMetaResponse.productListMetaWrapper.productListMetaData))
            filterViewModels.add(mapEtalaseResponseToEtalaseOptions(combinedData.shopEtalase))
            filterViewModels.add(mapCategoryResponseToCategoryOptions(combinedData.categoriesResponse))
            filterViewModels.add(mapMetaResponseToFilterOptions(combinedData.productListMetaResponse.productListMetaWrapper.productListMetaData))
            return filterViewModels
        }

        fun mapFilterViewModelsToSelectViewModels(filterViewModel: FilterViewModel): List<SelectViewModel> {
            val selectViewModels = mutableListOf<SelectViewModel>()
            for(i in filterViewModel.names.indices) {
                selectViewModels.add(
                        SelectViewModel(
                                filterViewModel.names[i],
                                filterViewModel.id[i],
                                filterViewModel.selectData[i]
                        )
                )
            }
            return selectViewModels
        }

        fun mapFilterViewModelsToChecklistViewModels(filterViewModel: FilterViewModel): List<ChecklistViewModel> {
            val checklistViewModels = mutableListOf<ChecklistViewModel>()
            for(i in filterViewModel.names.indices) {
                checklistViewModels.add(
                        ChecklistViewModel(
                                filterViewModel.names[i],
                                filterViewModel.id[i],
                                filterViewModel.selectData[i]
                        )
                )
            }
            return checklistViewModels
        }

        private fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val names = mutableListOf<String>()
            val ids = mutableListOf<String>()
            val values = mutableListOf<String>()
            val selectData = mutableListOf<Boolean>()
            for (sort in productListMetaData.sorts) {
                if(sort.name.isNotEmpty()) {
                    names.add(sort.name)
                    ids.add(sort.id)
                    values.add(sort.value)
                    selectData.add(false)
                }
            }
            return FilterViewModel(SORT_HEADER, names, ids, values, selectData, isChipsShown = SHOW_CHIPS)
        }

        private fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): FilterViewModel {
            val names = mutableListOf<String>()
            val ids = mutableListOf<String>()
            val selectData = mutableListOf<Boolean>()
            for (etalase in etalaseResponse) {
                if(etalase.name.isNotEmpty()) {
                    names.add(etalase.name)
                    ids.add(etalase.id)
                    selectData.add(false)
                }
            }
            return FilterViewModel(ETALASE_HEADER, names, ids, selectData = selectData, isChipsShown = SHOW_CHIPS)
        }

        private fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): FilterViewModel {
            val categories = mutableListOf<String>()
            val ids = mutableListOf<String>()
            val selectData = mutableListOf<Boolean>()
            for (category in categoriesResponse.categories.categories) {
                if(category.name.isNotEmpty()) {
                    categories.add(category.name)
                    ids.add(category.id)
                    selectData.add(false)
                }
            }
            return FilterViewModel(CATEGORY_HEADER, categories, ids, selectData = selectData, isChipsShown = HIDE_CHIPS)
        }

        private fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val filters = mutableListOf<String>()
            val ids = mutableListOf<String>()
            val values = mutableListOf<String>()
            val selectData = mutableListOf<Boolean>()
            for (filter in productListMetaData.filters) {
                if(filter.name.isNotEmpty()) {
                    filters.add(filter.name)
                    ids.add(filter.id)
                    values.add(filter.value.toString())
                    selectData.add(false)
                }
            }
            return FilterViewModel(OTHER_FILTER_HEADER, filters, ids, values, selectData, HIDE_CHIPS)
        }
    }
}
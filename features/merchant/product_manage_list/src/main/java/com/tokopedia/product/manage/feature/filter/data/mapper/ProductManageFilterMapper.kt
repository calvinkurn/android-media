package com.tokopedia.product.manage.feature.filter.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.CombinedResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption
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
            for(i in filterViewModel.data.indices) {
                selectViewModels.add(
                        SelectViewModel(
                                filterViewModel.data[i].id,
                                filterViewModel.data[i].name,
                                filterViewModel.data[i].values,
                                filterViewModel.data[i].select
                        )
                )
            }
            return selectViewModels
        }

        fun mapFilterViewModelsToChecklistViewModels(filterViewModel: FilterViewModel): List<ChecklistViewModel> {
            val checklistViewModels = mutableListOf<ChecklistViewModel>()
            for(i in filterViewModel.data.indices) {
                checklistViewModels.add(
                        ChecklistViewModel(
                                filterViewModel.data[i].id,
                                filterViewModel.data[i].name,
                                filterViewModel.data[i].values,
                                filterViewModel.data[i].select
                        )
                )
            }
            return checklistViewModels
        }

        fun mapSelectViewModelsToFilterViewModel(key: String, selectViewModels: List<SelectViewModel>): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            for(selectViewModel in selectViewModels) {
                data.add(
                        FilterDataViewModel(
                                selectViewModel.id,
                                selectViewModel.name,
                                selectViewModel.value,
                                selectViewModel.isSelected
                        )
                )
            }
            return if(key == ProductManageFilterFragment.SORT_CACHE_MANAGER_KEY) {
                FilterViewModel(SORT_HEADER, data, SHOW_CHIPS)
            } else {
                FilterViewModel(ETALASE_HEADER, data, SHOW_CHIPS)
            }
        }

        fun mapChecklistViewModelsToFilterViewModel(key: String, checklistViewModels: List<ChecklistViewModel>, isChipsShown: Boolean): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            for(checklistViewModel in checklistViewModels) {
                data.add(
                        FilterDataViewModel(
                                checklistViewModel.id,
                                checklistViewModel.name,
                                checklistViewModel.value,
                                checklistViewModel.isSelected
                        )
                )
            }
            return if(key == ProductManageFilterFragment.CATEGORIES_CACHE_MANAGER_KEY) {
                FilterViewModel(CATEGORY_HEADER, data, isChipsShown)
            } else {
                FilterViewModel(OTHER_FILTER_HEADER, data, isChipsShown)
            }
        }

        private fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            for (sort in productListMetaData.sorts) {
                if(sort.name.isNotEmpty()) {
                    data.add(
                            FilterDataViewModel(
                                    sort.id,
                                    sort.name,
                                    sort.value,
                                    false
                            )
                    )
                }
            }
            return FilterViewModel(SORT_HEADER, data, SHOW_CHIPS)
        }

        private fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            for (etalase in etalaseResponse) {
                if(etalase.name.isNotEmpty()) {
                    data.add(
                            FilterDataViewModel(
                                    etalase.id,
                                    etalase.name
                            )
                    )
                }
            }
            return FilterViewModel(ETALASE_HEADER, data, SHOW_CHIPS)
        }

        private fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            for (category in categoriesResponse.categories.categories) {
                if(category.name.isNotEmpty()) {
                    data.add(
                            FilterDataViewModel(
                                    category.id,
                                    category.name
                            )
                    )
                }
            }
            return FilterViewModel(CATEGORY_HEADER, data, HIDE_CHIPS)
        }

        private fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val data = mutableListOf<FilterDataViewModel>()
            for (filter in productListMetaData.filters) {
                if(filter.name.isNotEmpty()) {
                    data.add(
                            FilterDataViewModel(
                                    filter.id,
                                    filter.name,
                                    filter.value.toString(),
                                    false
                            )
                    )
                }
            }
            return FilterViewModel(OTHER_FILTER_HEADER, data, HIDE_CHIPS)
        }
    }
}
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

        fun mapCombinedResultToFilterViewModels(combinedData: CombinedResponse): List<FilterViewModel> {
            val filterViewModels = mutableListOf<FilterViewModel>()
            filterViewModels.add(mapMetaResponseToSortOptions(combinedData.productListMetaResponse.productListMetaWrapper.productListMetaData))
            filterViewModels.add(mapEtalaseResponseToEtalaseOptions(combinedData.shopEtalase))
            filterViewModels.add(mapCategoryResponseToCategoryOptions(combinedData.categoriesResponse))
            filterViewModels.add(mapMetaResponseToFilterOptions(combinedData.productListMetaResponse.productListMetaWrapper.productListMetaData))
            return filterViewModels
        }

        private fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val names = mutableListOf<String>()
            val ids = mutableListOf<String>()
            val values = mutableListOf<String>()
            for (sort in productListMetaData.sorts) {
                if(sort.name.isNotEmpty()) {
                    names.add(sort.name)
                    ids.add(sort.id)
                    values.add(sort.value)
                }
            }
            return FilterViewModel(SORT_HEADER, names, ids, values)
        }

        private fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): FilterViewModel {
            val names = mutableListOf<String>()
            val ids = mutableListOf<String>()
            for (etalase in etalaseResponse) {
                if(etalase.name.isNotEmpty()) {
                    names.add(etalase.name)
                    ids.add(etalase.id)
                }
            }
            return FilterViewModel(ETALASE_HEADER, names, ids)
        }

        private fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): FilterViewModel {
            val categories = mutableListOf<String>()
            val ids = mutableListOf<String>()
            for (category in categoriesResponse.categories.categories) {
                if(category.name.isNotEmpty()) {
                    categories.add(category.name)
                    ids.add(category.id)
                }
            }
            return FilterViewModel(CATEGORY_HEADER, categories, ids)
        }

        private fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val filters = mutableListOf<String>()
            val ids = mutableListOf<String>()
            val values = mutableListOf<String>()
            for (filter in productListMetaData.filters) {
                if(filter.name.isNotEmpty()) {
                    filters.add(filter.name)
                    filters.add(filter.id)
                    filters.add(filter.value.toString())
                }
            }
            return FilterViewModel(OTHER_FILTER_HEADER, filters, ids, values)
        }
    }
}
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
            filterViewModels.add(mapMetaResponseToSortOptions(combinedData.productListMetaResponse.productListMetaData))
            filterViewModels.add(mapEtalaseResponseToEtalaseOptions(combinedData.shopEtalase))
            filterViewModels.add(mapCategoryResponseToCategoryOptions(combinedData.categoriesResponse))
            filterViewModels.add(mapMetaResponseToFilterOptions(combinedData.productListMetaResponse.productListMetaData))
            return filterViewModels
        }

        private fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val names = mutableListOf<String>()
            for (sort in productListMetaData.sorts) {
                if(sort.name.isNotEmpty())
                    names.add(sort.name)
            }
            return FilterViewModel(SORT_HEADER, names)
        }

        private fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): FilterViewModel {
            val names = mutableListOf<String>()
            for (etalase in etalaseResponse) {
                if(etalase.name.isNotEmpty())
                    names.add(etalase.name)
            }
            return FilterViewModel(ETALASE_HEADER, names)
        }

        private fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): FilterViewModel {
            val categories = mutableListOf<String>()
            for (category in categoriesResponse.categories.categories) {
                if(category.name.isNotEmpty())
                    categories.add(category.name)
            }
            return FilterViewModel(CATEGORY_HEADER, categories)
        }

        private fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): FilterViewModel {
            val filters = mutableListOf<String>()
            for (filter in productListMetaData.filters) {
                if(filter.name.isNotEmpty())
                    filters.add(filter.name)
            }
            return FilterViewModel(OTHER_FILTER_HEADER, filters)
        }
    }
}
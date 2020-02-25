package com.tokopedia.product.manage.feature.filter.data.mapper

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.CategoriesViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.EtalaseViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.OtherFilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SortViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

class ProductManageFilterMapper {

    companion object {
        fun mapMetaResponseToSortOptions(productListMetaData: ProductListMetaData): SortViewModel {
            val names = mutableListOf<String>()
            for (sort in productListMetaData.sorts) {
                if(sort.name.isNotEmpty())
                    names.add(sort.name)
            }
            return SortViewModel(names)
        }

        fun mapEtalaseResponseToEtalaseOptions(etalaseResponse: ArrayList<ShopEtalaseModel>): EtalaseViewModel {
            val names = mutableListOf<String>()
            for (etalase in etalaseResponse) {
                if(etalase.name.isNotEmpty())
                    names.add(etalase.name)
            }
            return EtalaseViewModel(names)
        }

        fun mapCategoryResponseToCategoryOptions(categoriesResponse: CategoriesResponse): CategoriesViewModel {
            val categories = mutableListOf<String>()
            for (category in categoriesResponse.categories.categories.categoriesLevelThree.children) {
                if(category.name.isNotEmpty())
                    categories.add(category.name)
            }
            return CategoriesViewModel(categories)
        }

        fun mapMetaResponseToFilterOptions(productListMetaData: ProductListMetaData): OtherFilterViewModel {
            val filters = mutableListOf<String>()
            for (filter in productListMetaData.filters) {
                if(filter.name.isNotEmpty())
                    filters.add(filter.name)
            }
            return OtherFilterViewModel(filters)
        }
    }
}
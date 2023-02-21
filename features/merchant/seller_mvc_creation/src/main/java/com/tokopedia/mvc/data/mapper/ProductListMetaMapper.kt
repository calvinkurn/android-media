package com.tokopedia.mvc.data.mapper


import com.tokopedia.mvc.data.response.ProductListMetaResponse
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductMetadata
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import javax.inject.Inject

class ProductListMetaMapper @Inject constructor() {

    fun map(response: ProductListMetaResponse): ProductMetadata {
        val sortOptions = response.productListMeta.data.sort.map { sort -> ProductSortOptions(sort.id, sort.name, sort.value) }
        val categories = response.productListMeta.data.shopCategories.map { category -> ProductCategoryOption(category.id, category.name, category.value) }
        return ProductMetadata(sortOptions, categories)
    }
}

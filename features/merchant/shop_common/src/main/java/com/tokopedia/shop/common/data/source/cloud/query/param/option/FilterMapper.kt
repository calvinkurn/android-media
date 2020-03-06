package com.tokopedia.shop.common.data.source.cloud.query.param.option

import com.tokopedia.shop.common.data.source.cloud.query.param.ProductListParam
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.*

class FilterMapper {

    companion object {
        private const val VALUE_TRUE = "true"

        fun mapToRequestParam(filterOptions: List<FilterOption>): List<ProductListParam> {
            return filterOptions.map { filter ->
                when (filter) {
                    is FilterByCategory -> ProductListParam(filter.id, "${filter.categoryIds}")
                    is FilterByMenu -> ProductListParam(filter.id, "${filter.menuIds}")
                    is FilterByCondition -> ProductListParam(filter.id, VALUE_TRUE)
                    is FilterByPage -> ProductListParam(filter.id, filter.page.toString())
                    is FilterByKeyword -> ProductListParam(filter.id, filter.keyword)
                }
            }
        }

        fun mapToRequestParam(sortOption: SortOption): ProductListParam {
            val sortId = sortOption.id.name
            val option = sortOption.option.name
            return ProductListParam(sortId, option)
        }
    }
}
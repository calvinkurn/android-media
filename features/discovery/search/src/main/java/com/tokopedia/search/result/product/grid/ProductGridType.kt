package com.tokopedia.search.result.product.grid

import com.tokopedia.discovery.common.constants.SearchConstant

sealed interface ProductGridType {
    object Staggered : ProductGridType
    object Fixed : ProductGridType

    companion object {

        fun getProductGridType(typeValue: String): ProductGridType {
            return when (typeValue) {
                SearchConstant.ProductListType.FIXED_GRID -> Fixed
                else -> Staggered
            }
        }
    }
}

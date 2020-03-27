package com.tokopedia.shop.common.data.source.cloud.query.param.option

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

sealed class FilterOption(val id: String) {

    sealed class FilterByCondition(id: String): FilterOption(id) {
        object NewOnly: FilterByCondition(NEW_ONLY)
        object UsedOnly: FilterByCondition(USED_ONLY)
        object EmptyStockOnly: FilterByCondition(EMPTY_STOCK_ONLY)
        object VariantOnly: FilterByCondition(VARIANT_ONLY)
        object CashBackOnly: FilterByCondition(CASH_BACK_ONLY)
        object WholesaleOnly: FilterByCondition(WHOLESALE_ONLY)
        object PreorderOnly: FilterByCondition(PRE_ORDER_ONLY)
        object FeaturedOnly: FilterByCondition(FEATURED_ONLY)
    }

    data class FilterByMenu(val menuIds: List<String>): FilterOption(MENU)
    data class FilterByCategory(val categoryIds: List<String>): FilterOption(CATEGORY)
    data class FilterByPage(val page: Int): FilterOption(PAGE)
    data class FilterByKeyword(val keyword: String): FilterOption(KEYWORD)
    data class FilterByStatus(val status: ProductStatus): FilterOption(STATUS)

    companion object FilterId {
        private const val MENU = "menu"
        private const val CATEGORY = "category"
        private const val PAGE = "page"
        private const val KEYWORD = "keyword"
        private const val STATUS = "status"

        const val NEW_ONLY = "isNewOnly"
        const val USED_ONLY = "isUsedOnly"
        const val EMPTY_STOCK_ONLY = "isEmptyStockOnly"
        const val VARIANT_ONLY = "isVariantOnly"
        const val CASH_BACK_ONLY = "isCashbackOnly"
        const val WHOLESALE_ONLY = "isWholesaleOnly"
        const val PRE_ORDER_ONLY = "isPreorderOnly"
        const val FEATURED_ONLY = "isFeaturedOnly"
    }
}
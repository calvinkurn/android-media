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
        object CampaignOnly: FilterByCondition(CAMPAIGN_ONLY)
        object StockAlertOnly: FilterByCondition(STOCK_ALERT_ONLY)
        object NotifyMeOnly: FilterByCondition(NOTIFY_ME_ONLY)
        object StockAvailableOnly: FilterByCondition(STOCK_AVAILABLE_ONLY)
        object ProductArchival: FilterByCondition(PRODUCT_ARCHIVAL)
        object ProductArchivedStatus: FilterByCondition(STATUS_PRODUCT_ARCHIVED)
        object ProductPotentialArchivedStatus: FilterByCondition(STATUS_PRODUCT_ARCHIVAL_POTENTIAL)
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

        private const val NEW_ONLY = "isNewOnly"
        private const val USED_ONLY = "isUsedOnly"
        private const val EMPTY_STOCK_ONLY = "isEmptyStockOnly"
        private const val VARIANT_ONLY = "isVariantOnly"
        private const val CASH_BACK_ONLY = "isCashbackOnly"
        private const val WHOLESALE_ONLY = "isWholesaleOnly"
        private const val PRE_ORDER_ONLY = "isPreorderOnly"
        private const val FEATURED_ONLY = "isFeaturedOnly"
        private const val CAMPAIGN_ONLY = "isCampaignOnly"
        private const val STOCK_ALERT_ONLY = "hasStockAlertOnly"
        const val NOTIFY_ME_ONLY = "haveNotifyMeOOS"
        private const val STOCK_AVAILABLE_ONLY = "isStockGuaranteed"
        private const val PRODUCT_ARCHIVAL = "isProductArchival"

        private const val STATUS_PRODUCT_ARCHIVED = "isArchived"
        private const val STATUS_PRODUCT_ARCHIVAL_POTENTIAL = "isInGracePeriod"
    }
}

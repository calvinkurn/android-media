package com.tokopedia.seller.search.common.plt

enum class GlobalSearchSellerPerformanceMonitoringType(
        val prepareMetric: String,
        val networkMetric: String,
        val renderMetric: String,
        val trace: String
) {
    SEARCH_SELLER(
            GlobalSearchSellerPltConst.SEARCH_SELLER_PREPARE,
            GlobalSearchSellerPltConst.SEARCH_SELLER_NETWORK,
            GlobalSearchSellerPltConst.SEARCH_SELLER_RENDER,
            GlobalSearchSellerPltConst.SEARCH_SELLER_TRACE
    )
}
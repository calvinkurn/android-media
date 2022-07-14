package com.tokopedia.search.result.product.pagination

interface Pagination {
    val startFrom: Int
    val totalData: Int
    fun clearData()
    fun hasNextPage(): Boolean
    fun isFirstPage(): Boolean
    fun isLastPage(): Boolean
}

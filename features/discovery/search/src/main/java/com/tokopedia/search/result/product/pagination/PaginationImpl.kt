package com.tokopedia.search.result.product.pagination

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject

@SearchScope
class PaginationImpl @Inject constructor(): Pagination {
    override var startFrom: Int = 0

    override var totalData: Int = 0

    override fun clearData() {
        startFrom = 0
        totalData = 0
    }

    override fun hasNextPage(): Boolean = startFrom < totalData

    override fun isFirstPage(): Boolean = startFrom == 0

    override fun isLastPage(): Boolean = !hasNextPage()

    fun incrementStart() {
        startFrom += SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toIntOrZero()
    }

    fun decrementStart() {
        startFrom -= SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toIntOrZero()
    }
}

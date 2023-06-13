package com.tokopedia.search.result.product.lastfilter

interface LastFilterListener {

    fun onImpressedLastFilter(lastFilterDataView: LastFilterDataView)

    fun applyLastFilter(lastFilterDataView: LastFilterDataView)

    fun closeLastFilter(lastFilterDataView: LastFilterDataView)

    fun updateLastFilter()
}

package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.filter.common.data.SavedOption

interface LastFilterPresenter {
    fun updateLastFilter(
        searchParameter: Map<String, Any>,
        savedOptionList: List<SavedOption>,
    )
    fun closeLastFilter(searchParameter: Map<String, Any>)
}
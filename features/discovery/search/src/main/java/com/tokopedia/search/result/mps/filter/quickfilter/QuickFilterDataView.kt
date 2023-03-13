package com.tokopedia.search.result.mps.filter.quickfilter

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option

data class QuickFilterDataView(
    val filter: Filter = Filter()
) {

    val firstOption: Option?
        get() = filter.options.firstOrNull()
}

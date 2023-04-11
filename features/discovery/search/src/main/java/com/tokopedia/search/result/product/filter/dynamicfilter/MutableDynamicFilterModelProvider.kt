package com.tokopedia.search.result.product.filter.dynamicfilter

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.product.DynamicFilterModelProvider

interface MutableDynamicFilterModelProvider : DynamicFilterModelProvider {
    override var dynamicFilterModel: DynamicFilterModel?
}

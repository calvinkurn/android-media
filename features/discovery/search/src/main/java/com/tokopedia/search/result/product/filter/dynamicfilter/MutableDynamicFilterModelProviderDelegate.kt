package com.tokopedia.search.result.product.filter.dynamicfilter

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.product.DynamicFilterModelProvider

class MutableDynamicFilterModelProviderDelegate : MutableDynamicFilterModelProvider {
    override var dynamicFilterModel: DynamicFilterModel? = null
}

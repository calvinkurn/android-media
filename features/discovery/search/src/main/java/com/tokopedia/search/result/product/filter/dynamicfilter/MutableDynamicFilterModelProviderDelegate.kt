package com.tokopedia.search.result.product.filter.dynamicfilter

import com.tokopedia.filter.common.data.DynamicFilterModel
import javax.inject.Inject

class MutableDynamicFilterModelProviderDelegate @Inject constructor() : MutableDynamicFilterModelProvider {
    override var dynamicFilterModel: DynamicFilterModel? = null
}

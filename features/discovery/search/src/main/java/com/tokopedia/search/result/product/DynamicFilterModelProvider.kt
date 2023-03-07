package com.tokopedia.search.result.product

import com.tokopedia.filter.common.data.DynamicFilterModel

interface DynamicFilterModelProvider {
    val dynamicFilterModel: DynamicFilterModel?
}
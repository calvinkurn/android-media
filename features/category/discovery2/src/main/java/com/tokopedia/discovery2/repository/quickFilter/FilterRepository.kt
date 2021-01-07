package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.filter.common.data.DynamicFilterModel

interface FilterRepository {
    suspend fun getFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): DynamicFilterModel?
}
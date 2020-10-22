package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.filter.common.data.DynamicFilterModel

interface QuickFilterRepository {
    suspend fun getQuickFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): DynamicFilterModel?
}
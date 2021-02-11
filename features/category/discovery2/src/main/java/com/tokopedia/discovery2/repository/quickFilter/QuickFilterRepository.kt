package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.filter.common.data.Filter

interface QuickFilterRepository {
    suspend fun getQuickFilterData(componentId: String, pageEndPoint: String): ArrayList<Filter>?
}
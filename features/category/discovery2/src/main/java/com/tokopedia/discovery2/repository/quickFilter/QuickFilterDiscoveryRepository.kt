package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.filter.common.data.Filter

class QuickFilterDiscoveryRepository : BaseRepository(), QuickFilterRepository{
    override suspend fun getQuickFilterData(componentId: String, pageEndPoint: String): ArrayList<Filter>? {
        return arrayListOf()
    }
}
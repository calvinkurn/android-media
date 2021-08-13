package com.tokopedia.discovery2.repository.childcategory

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.ComponentsItem
import javax.inject.Inject

class DiscoveryChildCategoryRepository @Inject constructor() : BaseRepository(), ChildCategoryRepository {
    override suspend fun getChildCategory(componentId: String, pageEndPoint: String): List<ComponentsItem> {
        return arrayListOf()
    }
}
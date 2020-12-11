package com.tokopedia.discovery2.repository.childcategory

import com.tokopedia.discovery2.data.ComponentsItem

interface ChildCategoryRepository {
    suspend fun getChildCategory(componentId: String, pageEndPoint: String): List<ComponentsItem>
}
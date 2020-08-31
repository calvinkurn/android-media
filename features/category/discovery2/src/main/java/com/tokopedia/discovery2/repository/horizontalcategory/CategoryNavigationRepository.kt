package com.tokopedia.discovery2.repository.horizontalcategory

import com.tokopedia.discovery2.data.ComponentsItem

interface CategoryNavigationRepository {
    suspend fun getCategoryNavigationData(categoryDetailUrl: String): ArrayList<ComponentsItem>
}
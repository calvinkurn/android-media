package com.tokopedia.discovery2.repository.supportingbrand

import com.tokopedia.discovery2.data.ComponentsItem

interface SupportingBrandRepository {
    suspend fun getData(
        componentId: String,
        pageIdentifier: String,
        filter: MutableMap<String, Any>
    ): Pair<List<ComponentsItem>, String?>
}

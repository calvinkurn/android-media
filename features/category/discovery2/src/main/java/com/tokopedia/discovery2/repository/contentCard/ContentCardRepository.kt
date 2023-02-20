package com.tokopedia.discovery2.repository.contentCard

import com.tokopedia.discovery2.data.ComponentsItem

interface ContentCardRepository {
    suspend fun getContentCards(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, contentCardComponentName: String?): Pair<ArrayList<ComponentsItem>,String?>
}

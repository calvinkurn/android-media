package com.tokopedia.discovery2.repository.chipfilter

import com.tokopedia.discovery2.data.ComponentsItem

interface ChipFilterRepository {
    suspend fun getChipFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, position : Int, componentName : String?): List<ComponentsItem>
}
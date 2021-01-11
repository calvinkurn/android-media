package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.datamapper.mapDiscoveryResponseToPageData
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor(private val discoveryPageRepository: DiscoveryPageRepository) {

    suspend fun getDiscoveryPageDataUseCase(pageIdentifier: String, queryParameterMap: MutableMap<String, String?>): DiscoveryPageData {
        return mapDiscoveryResponseToPageData(discoveryPageData[pageIdentifier]?.let {
            it
        } ?: discoveryPageRepository.getDiscoveryPageData(pageIdentifier).apply {
            discoveryPageData[pageIdentifier] = this
            componentMap = HashMap()

            /***Chip Filter Require parent ID to function. Need to check on this later.***/
//            component = ComponentsItem(id = "PARENT_ID",pageEndPoint = pageInfo.identifier?:"").apply {
//                componentMap[id] = this
//            }
        }, queryParameterMap)
    }

    fun clearPage(pageIdentifier: String) {
        discoveryPageData.remove(pageIdentifier)
    }
}

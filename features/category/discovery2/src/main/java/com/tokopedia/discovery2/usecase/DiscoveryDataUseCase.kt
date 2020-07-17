package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.datamapper.mapDiscoveryResponseToPageData
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor(private val discoveryPageRepository: DiscoveryPageRepository) {

    suspend fun getDiscoveryPageDataUseCase(pageIdentifier: String): DiscoveryPageData {
        return mapDiscoveryResponseToPageData(discoveryPageData[pageIdentifier]?.let {
            it
        } ?: discoveryPageRepository.getDiscoveryPageData(pageIdentifier).apply {
            discoveryPageData[pageIdentifier] = this
            componentMap = HashMap()
        })
    }

    fun clearPage(pageIdentifier: String) {
        discoveryPageData.remove(pageIdentifier)
    }
}

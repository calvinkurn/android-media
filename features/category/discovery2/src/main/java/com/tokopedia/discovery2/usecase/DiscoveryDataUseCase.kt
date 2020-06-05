package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.mapDiscoveryResponseToPageData
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor(private val discoveryPageRepository: DiscoveryPageRepository) {

    suspend fun getDiscoveryPageDataUseCase(pageIdentifier: String): DiscoveryPageData {
        val discoveryResponseData = discoveryPageRepository.getDiscoveryPageData(pageIdentifier).copy()
        discoveryResponseData.components
        return mapDiscoveryResponseToPageData(discoveryResponseData)
    }
}

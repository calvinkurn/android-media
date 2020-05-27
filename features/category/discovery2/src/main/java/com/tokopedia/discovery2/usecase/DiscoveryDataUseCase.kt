package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor(private val discoveryPageRepository: DiscoveryPageRepository) {

    suspend fun getDiscoveryPageDataUseCase(pageIdentifier: String): DiscoveryResponse {
        val discoveryResponseData = discoveryPageRepository.getDiscoveryPageData(pageIdentifier).copy()
        discoveryResponseData.components
        return removeComponents(discoveryResponseData)
    }

    private fun removeComponents(discoveryPageData: DiscoveryResponse): DiscoveryResponse {
        if (!discoveryPageData.components.isNullOrEmpty()) {
            val componentsList = discoveryPageData.components!!.filter { it.renderByDefault }
            discoveryPageData.components = componentsList
        }
        return discoveryPageData
    }
}

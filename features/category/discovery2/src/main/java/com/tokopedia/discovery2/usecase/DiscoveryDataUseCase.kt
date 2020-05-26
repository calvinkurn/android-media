package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor(private val discoveryPageRepository: DiscoveryPageRepository) {

    suspend fun getDiscoveryPageDataUseCase(pageIdentifier: String): DiscoveryResponse {
        return removeComponents(discoveryPageRepository.getDiscoveryPageData(pageIdentifier))
    }

    private fun removeComponents(discoveryPageData: DiscoveryResponse): DiscoveryResponse {
        if (!discoveryPageData.components.isNullOrEmpty()) {
            val componentsList = discoveryPageData.components.filter { it.renderByDefault }
            discoveryPageData.components.clear()
            discoveryPageData.components.addAll(componentsList)
        }
        return discoveryPageData
    }
}

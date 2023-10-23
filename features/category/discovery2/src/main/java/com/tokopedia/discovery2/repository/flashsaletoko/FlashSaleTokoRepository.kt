package com.tokopedia.discovery2.repository.flashsaletoko

import com.tokopedia.discovery2.data.DiscoveryResponse

interface FlashSaleTokoRepository {
    suspend fun getTabData(componentId: String, pageIdentifier: String): DiscoveryResponse
}

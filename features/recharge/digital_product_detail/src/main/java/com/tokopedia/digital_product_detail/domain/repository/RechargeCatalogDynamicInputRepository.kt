package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogDynamicInput

interface RechargeCatalogDynamicInputRepository {
    suspend fun getDynamicInput(menuID: Int, operator: String): DigitalCatalogDynamicInput
}
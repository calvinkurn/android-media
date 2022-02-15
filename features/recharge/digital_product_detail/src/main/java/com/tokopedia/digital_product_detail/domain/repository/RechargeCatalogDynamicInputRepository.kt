package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.data.product.CatalogProduct

interface RechargeCatalogDynamicInputRepository {
    suspend fun getDynamicInputTagihanListrik(menuID: Int, operator: String): CatalogProduct?
}
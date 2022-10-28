package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup

interface RechargeCatalogOperatorSelectGroupRepository {
    suspend fun getOperatorSelectGroup(menuId: Int): DigitalCatalogOperatorSelectGroup
}
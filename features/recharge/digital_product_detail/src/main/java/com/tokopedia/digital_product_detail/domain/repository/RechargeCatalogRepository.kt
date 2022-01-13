package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

interface RechargeCatalogRepository {
    suspend fun getCatalogRepository(menuId: Int, operator: String): DenomWidgetModel
}
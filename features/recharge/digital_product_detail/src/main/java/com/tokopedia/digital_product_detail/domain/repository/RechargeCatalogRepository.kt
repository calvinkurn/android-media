package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.recharge_component.model.denom.DenomMCCMModel

interface RechargeCatalogRepository {
    suspend fun getDenomGridList(menuId: Int, operator: String): DenomMCCMModel
}
package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel

interface RechargeCatalogProductInputMultiTabRepository {
    suspend fun getProductInputMultiTabDenomFull(menuID: Int, operatorId: String, clientNumber: String,
                                             filterData: ArrayList<HashMap<String, Any>>?): InputMultiTabDenomModel

    suspend fun getProductInputMultiTabDenomGrid(menuID: Int, operatorId: String, clientNumber: String): DenomMCCMModel
}
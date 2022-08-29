package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

interface RechargeCatalogProductInputMultiTabRepository {
    suspend fun getProductInputMultiTabDenomFull(menuID: Int, operatorId: String, clientNumber: String,
                                             filterData: ArrayList<HashMap<String, Any>>?, isFilterRefreshed: Boolean = true): InputMultiTabDenomModel

    suspend fun getProductInputMultiTabDenomGrid(menuID: Int, operatorId: String, clientNumber: String): DenomMCCMModel

    suspend fun getProductTokenListrikDenomGrid(menuID: Int, operatorId: String, clientNumber: String): DenomWidgetModel

    suspend fun getProductTagihanListrik(menuID: Int, operatorId: String, clientNumber: String): RechargeProduct?
}
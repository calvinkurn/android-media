package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel

interface RechargeCatalogProductInputMultiTabRepository {
    suspend fun getProductInputMultiTabDenom(menuID: Int, operatorId: String, clientNumber: String,
                                             filterData: ArrayList<HashMap<String, Any>>?): InputMultiTabDenomModel
}
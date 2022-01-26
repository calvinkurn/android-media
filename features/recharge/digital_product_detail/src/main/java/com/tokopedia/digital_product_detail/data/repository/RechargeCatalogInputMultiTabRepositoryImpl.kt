package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogProductInputMultiTabRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogInputMultiTabUseCase

class RechargeCatalogInputMultiTabRepositoryImpl(
    private val getRechargeCatalogInputMultiTabUseCase: GetRechargeCatalogInputMultiTabUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogProductInputMultiTabRepository {
    override suspend fun getProductInputMultiTabDenom(
        menuID: Int,
        operatorId: String,
        clientNumber: String,
        filterData: ArrayList<HashMap<String, Any>>?
    ): InputMultiTabDenomModel {
        return
    }
}
package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomFullUIMapper
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogProductInputMultiTabRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogInputMultiTabUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogInputMultiTabRepositoryImpl @Inject constructor(
    private val getRechargeCatalogInputMultiTabUseCase: GetRechargeCatalogInputMultiTabUseCase,
    private val mapper: DigitalDenomFullUIMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogProductInputMultiTabRepository {
    override suspend fun getProductInputMultiTabDenom(
        menuID: Int,
        operatorId: String,
        clientNumber: String,
        filterData: ArrayList<HashMap<String, Any>>?
    ) = withContext(dispatchers.io){
        val catalog = getRechargeCatalogInputMultiTabUseCase.apply {
            createProductListParams(menuID, operatorId, clientNumber, filterData)
        }.executeOnBackground()

        return@withContext mapper.mapMultiTabDenom(catalog)
    }
}
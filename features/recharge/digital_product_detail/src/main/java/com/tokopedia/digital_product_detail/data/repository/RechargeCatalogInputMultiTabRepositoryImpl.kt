package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogProductInputMultiTabRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogInputMultiTabUseCase
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogInputMultiTabRepositoryImpl @Inject constructor(
    private val getRechargeCatalogInputMultiTabUseCase: GetRechargeCatalogInputMultiTabUseCase,
    private val mapper: DigitalDenomMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogProductInputMultiTabRepository {

    override suspend fun getProductInputMultiTabDenomFull(
        menuID: Int,
        operatorId: String,
        clientNumber: String,
        filterData: ArrayList<HashMap<String, Any>>?,
        isFilterRefreshed: Boolean
    ) = withContext(dispatchers.io){
        val data = getRechargeCatalogInputMultiTabUseCase.apply {
            createProductListParams(menuID, operatorId, clientNumber, filterData)
        }.executeOnBackground()

        return@withContext mapper.mapMultiTabFullDenom(data, isFilterRefreshed)
    }

    override suspend fun getProductInputMultiTabDenomGrid(
        menuID: Int,
        operatorId: String,
        clientNumber: String
    )= withContext(dispatchers.io){
        val data = getRechargeCatalogInputMultiTabUseCase.apply {
            createProductListParams(menuID, operatorId, clientNumber, null)
        }.executeOnBackground()

        return@withContext mapper.mapMultiTabGridDenom(data)
    }

    override suspend fun getProductTokenListrikDenomGrid(
        menuID: Int,
        operatorId: String,
        clientNumber: String
    ): DenomWidgetModel = withContext(dispatchers.io){
        val data = getRechargeCatalogInputMultiTabUseCase.apply {
            createProductListParams(menuID, operatorId, clientNumber, null)
        }.executeOnBackground()
        return@withContext mapper.mapTokenListrikDenom(data)
    }

    override suspend fun getProductTagihanListrik(
        menuID: Int,
        operatorId: String,
        clientNumber: String
    ): RechargeProduct? = withContext(dispatchers.io){
        val data = getRechargeCatalogInputMultiTabUseCase.apply {
            createProductListParams(menuID, operatorId, clientNumber, null)
        }.executeOnBackground()
        return@withContext mapper.mapTagihanListrikProduct(data)
    }
}
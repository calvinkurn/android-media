package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.domain.repository.RechargeMCCMProductsRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeMCCMUseCase
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeMCCMProductsRepositoryImpl @Inject constructor(
    private val getRechargeMCCMUseCase: GetRechargeMCCMUseCase,
    private val mapper: DigitalDenomMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeMCCMProductsRepository {

    override suspend fun getMCCMProducts(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String
    ): DenomWidgetModel = withContext(dispatchers.io) {
        val data = getRechargeMCCMUseCase.execute(clientNumbers, dgCategoryIds, dgOperatorIds, channelName)
        return@withContext mapper.mapDigiPersoToMCCMProducts(data.digitalPersoData)
    }
}

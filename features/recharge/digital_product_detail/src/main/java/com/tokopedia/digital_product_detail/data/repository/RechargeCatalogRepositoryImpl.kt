package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomGridUiMapper
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogUseCase
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogRepositoryImpl @Inject constructor(
    private val getRechargeCatalogUseCase: GetRechargeCatalogUseCase,
    private val mapper: DigitalDenomGridUiMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogRepository {

    override suspend fun getDenomList(menuId: Int, operator: String) = withContext(dispatchers.io) {
        val catalog = getRechargeCatalogUseCase.apply {
            params = GetRechargeCatalogUseCase.createProductListParams(menuId, operator)
        }.executeOnBackground()

        return@withContext mapper.mapCatalogDenom(catalog.response)
    }

}
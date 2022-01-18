package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMCCMGridUiMapper
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogRepositoryImpl @Inject constructor(
    private val getRechargeCatalogUseCase: GetRechargeCatalogUseCase,
    private val mapper: DigitalDenomMCCMGridUiMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogRepository {

    override suspend fun getDenomGridList(menuId: Int, operator: String) = withContext(dispatchers.io) {
        val catalog = getRechargeCatalogUseCase.apply {
            params = GetRechargeCatalogUseCase.createProductListParams(menuId, operator)
        }.executeOnBackground()

        return@withContext mapper.mapCatalogDenom(catalog.response)
    }

}
package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.digital_product_detail.data.mapper.DigitalDynamicInputMapper
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogDynamicInput
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogDynamicInputRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogDynamicInputUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogDynamicInputRepositoryImpl @Inject constructor(
    private val getRechargeCatalogDynamicInputUseCase: GetRechargeCatalogDynamicInputUseCase,
    private val mapper: DigitalDynamicInputMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogDynamicInputRepository{

    override suspend fun getDynamicInputTagihanListrik(
        menuID: Int,
        operator: String
    ): CatalogProduct? = withContext(dispatchers.io){
         val data = getRechargeCatalogDynamicInputUseCase.apply {
            createDynamicInput(menuID, operator)
        }.executeOnBackground()

        return@withContext mapper.mapDynamicInputProduct(data.response)
    }

}
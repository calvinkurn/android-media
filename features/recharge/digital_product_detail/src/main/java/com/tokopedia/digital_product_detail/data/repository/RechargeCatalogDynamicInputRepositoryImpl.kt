package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogDynamicInput
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogDynamicInputRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogDynamicInputUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogDynamicInputRepositoryImpl @Inject constructor(
    private val getRechargeCatalogDynamicInputUseCase: GetRechargeCatalogDynamicInputUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogDynamicInputRepository{

    override suspend fun getDynamicInput(
        menuID: Int,
        operator: String
    ): DigitalCatalogDynamicInput = withContext(dispatchers.io){
        return@withContext getRechargeCatalogDynamicInputUseCase.apply {
            createDynamicInput(menuID, operator)
        }.executeOnBackground()
    }

}
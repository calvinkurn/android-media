package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogOperatorSelectGroupRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogOperatorSelectGroupUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogOperatorSelectGroupRepositoryImpl @Inject constructor(
    private val getRechargeCatalogOperatorSelectGroupUseCase: GetRechargeCatalogOperatorSelectGroupUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogOperatorSelectGroupRepository {

    override suspend fun getOperatorSelectGroup(menuId: Int): DigitalCatalogOperatorSelectGroup = withContext(dispatchers.io){
        return@withContext getRechargeCatalogOperatorSelectGroupUseCase.apply {
            createParams(menuId)
        }.executeOnBackground()
    }
}
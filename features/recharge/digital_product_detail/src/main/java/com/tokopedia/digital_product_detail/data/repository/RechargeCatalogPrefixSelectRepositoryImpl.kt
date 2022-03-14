package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogPrefixSelectUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogPrefixSelectRepositoryImpl @Inject constructor(
    private val getRechargeCatalogPrefixSelectUseCase: GetRechargeCatalogPrefixSelectUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogPrefixSelectRepository {

    override suspend fun getOperatorList(
        menuId: Int
    ): TelcoCatalogPrefixSelect = withContext(dispatchers.io) {
        return@withContext getRechargeCatalogPrefixSelectUseCase.apply {
            setRequestParam(menuId)
        }.executeOnBackground()
    }
}
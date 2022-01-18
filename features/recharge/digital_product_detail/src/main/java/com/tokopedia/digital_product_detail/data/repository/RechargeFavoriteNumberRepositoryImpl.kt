package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeFavoriteNumberUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeFavoriteNumberRepositoryImpl @Inject constructor(
    private val getRechargeFavoriteNumberUseCase: GetRechargeFavoriteNumberUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeFavoriteNumberRepository {

    override suspend fun getFavoriteNumber(
        categoryIds: List<String>
    ): TopupBillsSeamlessFavNumberData = withContext(dispatchers.io) {

        return@withContext getRechargeFavoriteNumberUseCase.apply {
            setRequestParams(categoryIds)
        }.executeOnBackground()
    }
}
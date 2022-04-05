package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.common.topupbills.favorite.domain.usecase.GetRechargeFavoriteNumberUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeFavoriteNumberRepositoryImpl @Inject constructor(
    private val getRechargeFavoriteNumberUseCase: GetRechargeFavoriteNumberUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeFavoriteNumberRepository {

    override suspend fun getFavoriteNumberChips(
        categoryIds: List<Int>,
        operatorIds: List<Int>
    ): TopupBillsPersoFavNumberData = withContext(dispatchers.io) {

        return@withContext getRechargeFavoriteNumberUseCase.apply {
            setRequestParams(categoryIds, operatorIds, CHANNEL_FAVORITE_NUMBER_CHIP)
        }.executeOnBackground()
    }

    override suspend fun getFavoriteNumberList(
        categoryIds: List<Int>,
        operatorIds: List<Int>
    ): TopupBillsPersoFavNumberData = withContext(dispatchers.io) {

        return@withContext getRechargeFavoriteNumberUseCase.apply {
            setRequestParams(categoryIds, operatorIds, CHANNEL_FAVORITE_NUMBER_LIST)
        }.executeOnBackground()
    }

    companion object {
        const val CHANNEL_FAVORITE_NUMBER_CHIP = "favorite_number_chips"
        const val CHANNEL_FAVORITE_NUMBER_LIST = "favorite_number_list"
    }
}
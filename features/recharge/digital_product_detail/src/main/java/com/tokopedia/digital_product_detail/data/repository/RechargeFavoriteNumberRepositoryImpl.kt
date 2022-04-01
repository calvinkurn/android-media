package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberGroup
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeFavoriteNumberUseCase
import com.tokopedia.digital_product_detail.domain.util.FavoriteNumberType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeFavoriteNumberRepositoryImpl @Inject constructor(
    private val getRechargeFavoriteNumberUseCase: GetRechargeFavoriteNumberUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeFavoriteNumberRepository {

    override suspend fun getFavoriteNumbers(
        favoriteNumberTypes: List<FavoriteNumberType>,
        categoryIds: List<Int>,
        operatorIds: List<Int>
    ): PersoFavNumberGroup = withContext(dispatchers.io) {
        return@withContext getRechargeFavoriteNumberUseCase.apply {
            clearRequests()
            favoriteNumberTypes.forEach {
                addFavoriteNumberByChannel(it, categoryIds, operatorIds)
            }
        }.executeOnBackground()
    }
}
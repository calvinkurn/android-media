package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalPersoMapper
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.digital_product_detail.domain.model.FavoriteGroupModel
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeFavoriteNumberUseCase
import com.tokopedia.digital_product_detail.domain.util.FavoriteNumberType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeFavoriteNumberRepositoryImpl @Inject constructor(
    private val getRechargeFavoriteNumberUseCase: GetRechargeFavoriteNumberUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val digitalPersoMapper: DigitalPersoMapper
): RechargeFavoriteNumberRepository {

    override suspend fun getFavoriteNumbers(
        favoriteNumberTypes: List<FavoriteNumberType>,
        categoryIds: List<Int>,
        operatorIds: List<Int>
    ): FavoriteGroupModel = withContext(dispatchers.io) {
        val data = getRechargeFavoriteNumberUseCase.apply {
            clearRequests()
            favoriteNumberTypes.forEach {
                addFavoriteNumberByChannel(it, categoryIds, operatorIds)
            }
        }.executeOnBackground()

        return@withContext digitalPersoMapper.mapDigiPersoFavoriteToModel(data)
    }
}
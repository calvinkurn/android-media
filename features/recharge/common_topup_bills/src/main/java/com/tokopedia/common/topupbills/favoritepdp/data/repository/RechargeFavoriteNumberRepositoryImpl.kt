package com.tokopedia.common.topupbills.favoritepdp.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.favoritepdp.data.mapper.DigitalPersoMapper
import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.domain.usecase.GetRechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
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
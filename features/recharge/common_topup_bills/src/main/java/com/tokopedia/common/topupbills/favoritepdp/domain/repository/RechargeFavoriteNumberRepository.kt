package com.tokopedia.common.topupbills.favoritepdp.domain.repository

import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteGroupModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType

interface RechargeFavoriteNumberRepository {
    suspend fun getFavoriteNumbers(
        favoriteNumberTypes: List<FavoriteNumberType>,
        categoryIds: List<Int>,
        operatorIds: List<Int> = emptyList(),
    ): FavoriteGroupModel
}
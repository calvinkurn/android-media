package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberGroup
import com.tokopedia.digital_product_detail.domain.util.FavoriteNumberType

interface RechargeFavoriteNumberRepository {
    suspend fun getFavoriteNumbers(
        favoriteNumberTypes: List<FavoriteNumberType>,
        categoryIds: List<Int>,
        operatorIds: List<Int> = emptyList(),
    ): PersoFavNumberGroup

//    suspend fun getFavoriteNumberAll(
//        favoriteNumberTypes: List<RechargeFavoriteNumberRepositoryImpl.FavoriteNumberType>,
//        categoryIds: List<Int>,
//        operatorIds: List<Int> = emptyList(),
//    ): PersoFavNumberGroup
//
//    suspend fun getFavoriteNumberChipsList(
//        favoriteNumberTypes: List<RechargeFavoriteNumberRepositoryImpl.FavoriteNumberType>,
//        categoryIds: List<Int>,
//        operatorIds: List<Int> = emptyList()
//    ): PersoFavNumberGroup
}
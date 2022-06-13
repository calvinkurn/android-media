package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.digital_product_detail.domain.model.FavoriteGroupModel
import com.tokopedia.digital_product_detail.domain.util.FavoriteNumberType

interface RechargeFavoriteNumberRepository {
    suspend fun getFavoriteNumbers(
        favoriteNumberTypes: List<FavoriteNumberType>,
        categoryIds: List<Int>,
        operatorIds: List<Int> = emptyList(),
    ): FavoriteGroupModel
}
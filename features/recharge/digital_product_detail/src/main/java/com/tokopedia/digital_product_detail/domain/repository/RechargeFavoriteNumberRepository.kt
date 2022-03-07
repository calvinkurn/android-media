package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData

interface RechargeFavoriteNumberRepository {
    suspend fun getFavoriteNumberChips(
        categoryIds: List<Int>,
        operatorIds: List<Int> = listOf()
    ): TopupBillsPersoFavNumberData
}
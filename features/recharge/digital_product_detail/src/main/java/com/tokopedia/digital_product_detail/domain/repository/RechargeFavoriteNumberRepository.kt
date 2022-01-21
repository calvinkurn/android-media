package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberData

interface RechargeFavoriteNumberRepository {
    suspend fun getFavoriteNumber(categoryIds: List<Int>): TopupBillsPersoFavNumberData
}
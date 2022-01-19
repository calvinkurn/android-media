package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData

interface RechargeFavoriteNumberRepository {
    suspend fun getFavoriteNumber(categoryIds: List<String>): TopupBillsSeamlessFavNumberData
}
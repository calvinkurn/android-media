package com.tokopedia.shop.score.view.mapper

import com.tokopedia.shop.score.domain.model.ShopScoreResult
import com.tokopedia.shop.score.view.model.ShopScoreDetailData
import com.tokopedia.shop.score.view.model.ShopScoreDetailItem
import com.tokopedia.shop.score.view.model.ShopScoreDetailSummary
import com.tokopedia.shop.score.view.model.ShopType
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopScoreDetailMapper @Inject constructor(
    private val userSession: UserSessionInterface
) {

    fun mapToShopScoreDetailData(result: ShopScoreResult): ShopScoreDetailData {
        val shopType = getShopType()
        val summary = getShopScoreSummary(result)
        val items = getShopScoreItems(result)

        return ShopScoreDetailData(shopType, summary, items)
    }

    private fun getShopType(): ShopType {
        return when {
            userSession.isShopOfficialStore -> ShopType.OFFICIAL_STORE
            userSession.isGoldMerchant -> ShopType.POWER_MERCHANT
            !userSession.isGoldMerchant -> ShopType.REGULAR_MERCHANT
            else -> ShopType.UNKNOWN
        }
    }

    private fun getShopScoreSummary(result: ShopScoreResult): ShopScoreDetailSummary? {
        return result.shopScoreSummary?.let {
            ShopScoreDetailSummary(
                it.color,
                it.value,
                it.title
            )
        }
    }

    private fun getShopScoreItems(result: ShopScoreResult): List<ShopScoreDetailItem> {
        return result.shopScoreDetail.map {
            ShopScoreDetailItem(
                it.title,
                it.value,
                it.maxValue,
                it.description,
                it.color
            )
        }
    }
}
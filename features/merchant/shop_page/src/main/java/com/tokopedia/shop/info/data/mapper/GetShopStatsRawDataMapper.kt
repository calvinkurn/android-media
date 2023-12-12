package com.tokopedia.shop.info.data.mapper

import com.tokopedia.shop.info.data.response.GetShopStatsRawDataResponse
import com.tokopedia.shop.info.domain.entity.ShopStatsRawData
import javax.inject.Inject

class GetShopStatsRawDataMapper @Inject constructor() {
    fun map(response: GetShopStatsRawDataResponse): ShopStatsRawData {
        return ShopStatsRawData(
            chatAndDiscussionReplySpeed = response.getShopStatsRawData.result.chatAndDiscussionReplySpeed
        )
    }
}

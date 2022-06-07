package com.tokopedia.shop.flashsale.domain.entity.aggregate

import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner
import com.tokopedia.shop.flashsale.domain.entity.ShopInfo

data class ShareComponentMetadata(
    val banner: CampaignBanner,
    val shop: ShopInfo
)
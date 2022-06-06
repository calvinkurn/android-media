package com.tokopedia.shop.flash_sale.domain.entity.aggregate

import com.tokopedia.shop.flash_sale.domain.entity.CampaignBanner
import com.tokopedia.shop.flash_sale.domain.entity.ShopInfo

data class ShareComponentMetadata(
    val banner: CampaignBanner,
    val shop: ShopInfo
)
package com.tokopedia.shop.flashsale.domain.entity.aggregate

import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.ShopInfo

data class CampaignPrerequisiteData(
    val drafts: List<CampaignUiModel>,
    val remainingQuota: Int,
    val shopInfo: ShopInfo
)
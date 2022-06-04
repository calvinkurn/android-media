package com.tokopedia.shop.flash_sale.domain.entity.aggregate

import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel

data class CampaignPrerequisiteData(val drafts: List<CampaignUiModel>, val remainingQuota: Int)
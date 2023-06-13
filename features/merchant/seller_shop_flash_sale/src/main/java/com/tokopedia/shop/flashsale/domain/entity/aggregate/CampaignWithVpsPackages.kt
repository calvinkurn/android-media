package com.tokopedia.shop.flashsale.domain.entity.aggregate

import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel

data class CampaignWithVpsPackages(
    val campaign: CampaignUiModel,
    val vpsPackages: List<VpsPackageUiModel>
)
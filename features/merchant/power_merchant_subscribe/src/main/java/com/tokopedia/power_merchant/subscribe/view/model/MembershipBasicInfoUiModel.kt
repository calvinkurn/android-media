package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel

/**
 * Created by @ilhamsuaib on 25/05/22.
 */

data class MembershipBasicInfoUiModel(
    val headerData: MembershipDetailUiModel,
    val pmShopInfo: PMShopInfoUiModel
)
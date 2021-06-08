package com.tokopedia.power_merchant.subscribe.view_old.model

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus

/**
 * Created By @ilhamsuaib on 27/03/21
 */

data class PMStatusAndSettingUiModel(
        val pmStatus: PowerMerchantStatus?,
        val pmSettingAndShopInfo: PMSettingAndShopInfoUiModel
)
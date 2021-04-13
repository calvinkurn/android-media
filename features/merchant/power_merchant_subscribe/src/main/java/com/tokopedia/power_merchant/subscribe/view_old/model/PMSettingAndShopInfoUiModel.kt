package com.tokopedia.power_merchant.subscribe.view_old.model

import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel

/**
 * Created By @ilhamsuaib on 27/03/21
 */

data class PMSettingAndShopInfoUiModel(
        val shopInfo: PMShopInfoUiModel,
        val pmSetting: PowerMerchantSettingInfoUiModel
)
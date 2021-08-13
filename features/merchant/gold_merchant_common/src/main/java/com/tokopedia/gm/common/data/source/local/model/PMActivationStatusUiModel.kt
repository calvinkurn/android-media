package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PMConstant

/**
 * Created By @ilhamsuaib on 19/05/21
 */

data class PMActivationStatusUiModel(
        val isSuccess: Boolean = false,
        val message: String = "",
        val currentShopTier: Int = PMConstant.ShopTierType.POWER_MERCHANT
)
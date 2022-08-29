package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created By @ilhamsuaib on 19/05/21
 */

data class PMActivationStatusUiModel(
    val isSuccess: Boolean = false,
    val message: String = String.EMPTY,
    val currentShopTier: Int = PMConstant.ShopTierType.POWER_MERCHANT,
    val errorCode: String = String.EMPTY
) {

    fun shouldUpdateApp(): Boolean {
        return errorCode == PMConstant.APP_UPDATE_ERROR_CODE
    }
}
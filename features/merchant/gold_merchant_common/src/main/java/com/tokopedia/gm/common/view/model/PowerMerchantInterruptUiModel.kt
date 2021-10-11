package com.tokopedia.gm.common.view.model

import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PMTier

/**
 * Created By @ilhamsuaib on 20/03/21
 */

data class PowerMerchantInterruptUiModel(
        val shopScore: Int = 0,
        val shopScoreThreshold: Int = 0,
        val shopLevel: Int = 0,
        val isNewSeller: Boolean = true,
        val shopAge: Long = MIN_SHOP_AGE,
        val isEligiblePm: Boolean = false,
        val pmStatus: String = PMStatusConst.INACTIVE,
        val pmTier: Int = PMTier.REGULAR,
        val pmGrade: String = PMShopGrade.NEW_SELLER,
        val pmGradeBadge: String = "",
        val potentialPmGrade: String = "",
        val potentialPmGradeBadge: String = "",
        val pmNewUpdateDateFmt: String = "",
        val periodType: String = "",
        val periodStartDate: String = "",
        val hasReputation: Boolean = false,
        val isOfficialStore: Boolean = false
) {
    companion object {
        const val MIN_SHOP_AGE = 1L
    }
}
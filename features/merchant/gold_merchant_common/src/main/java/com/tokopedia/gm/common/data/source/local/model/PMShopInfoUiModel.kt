package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 09/03/21
 */

data class PMShopInfoUiModel(
        val isNewSeller: Boolean = true,
        val isKyc: Boolean = false,
        val kycStatusId: Int = 0, //pls refer https://tokopedia.atlassian.net/wiki/spaces/AUT/pages/452132984/KYC+-+Know+Your+Customer
        val shopScore: Int = 0,
        val shopScoreThreshold: Int = DEFAULT_PM_SHOP_SCORE_THRESHOLD,
        val shopScorePmProThreshold: Int = DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD,
        val hasActiveProduct: Boolean = false,
        val isEligiblePm: Boolean = false,
        val isEligiblePmPro: Boolean = false,
        val itemSoldOneMonth: Long = 0,
        val itemSoldPmProThreshold: Long = DEFAULT_ORDER_THRESHOLD,
        val netItemValueOneMonth: Long = 0,
        val netItemValuePmProThreshold: Long = DEFAULT_NIV_THRESHOLD
) {

    companion object {
        const val DEFAULT_PM_SHOP_SCORE_THRESHOLD = 60
        const val DEFAULT_PM_PRO_SHOP_SCORE_THRESHOLD = 70
        const val DEFAULT_ORDER_THRESHOLD = 3L
        const val DEFAULT_NIV_THRESHOLD = 350000L
    }

    fun isEligibleShopScore() = shopScore >= shopScoreThreshold

    fun isEligibleShopScorePmPro() = shopScore >= shopScorePmProThreshold
}
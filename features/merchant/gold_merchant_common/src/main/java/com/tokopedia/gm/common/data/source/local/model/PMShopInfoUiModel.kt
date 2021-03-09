package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 09/03/21
 */

data class PMShopInfoUiModel(
        val shopId: String = "",
        val isNewSeller: Boolean = true,
        val shopAge: Int = 0,
        val isKyc: Boolean = false,
        val kycStatusId: Int = 0, //pls refer https://tokopedia.atlassian.net/wiki/spaces/AUT/pages/452132984/KYC+-+Know+Your+Customer
        val shopScore: Int = 0,
        val shopScoreThreshold: Int = 0,
        val isEligibleShopScore: Boolean = false,
        val hasActiveProduct: Boolean = false,
        val isEligiblePm: Boolean = false,
        val shopLevel: Int? = 0
)
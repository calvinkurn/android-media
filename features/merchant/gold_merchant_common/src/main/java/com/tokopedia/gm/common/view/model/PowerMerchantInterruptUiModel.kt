package com.tokopedia.gm.common.view.model

/**
 * Created By @ilhamsuaib on 20/03/21
 */

data class PowerMerchantInterruptUiModel(
        val shopScore: Int,
        val shopScoreThreshold: Int,
        val shopLevel: Int,
        val isNewSeller: Boolean,
        val shopAge: Int,
        val isEligiblePm: Boolean,
        val pmStatus: String,
        val pmGrade: String,
        val pmGradeBadge: String,
        val potentialPmGrade: String,
        val potentialPmGradeBadge: String,
        val pmNewUpdateDateFmt: String,
        val periodType: String,
        val hasReputation: Boolean,
        val isOfficialStore: Boolean
)
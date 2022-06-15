package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

data class MembershipDataUiModel(
    val shopScore: Int = Int.ZERO,
    val totalOrder: Long = Int.ZERO.toLong(),
    val netIncome: Long = Int.ZERO.toLong(),
    val orderThreshold: Long = Int.ZERO.toLong(),
    val netIncomeThreshold: Long = Int.ZERO.toLong(),
    val netIncomeThresholdFmt: String = String.EMPTY,
    val gradeBenefit: PMGradeWithBenefitsUiModel
) {

    fun isEligibleIncome(): Boolean = netIncome >= netIncomeThreshold

    fun isEligibleOrder(): Boolean = totalOrder >= orderThreshold
}
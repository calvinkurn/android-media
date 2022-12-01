package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

data class MembershipDataUiModel(
    val shopScore: Int = Int.ZERO,
    val orderThreshold: Long = Int.ZERO.toLong(),
    val netIncomeThreshold: Long = Int.ZERO.toLong(),
    val netIncomeThresholdFmt: String = String.EMPTY,
    val gradeBenefit: PMGradeWithBenefitsUiModel,
    private val totalOrder: Long = Int.ZERO.toLong(),
    private val netIncome: Long = Int.ZERO.toLong()
) {

    fun isEligibleIncome(): Boolean = netIncome >= netIncomeThreshold

    fun isEligibleOrder(): Boolean = totalOrder >= orderThreshold

    fun getTotalOrderValue(): Long {
        return if (totalOrder < Int.ZERO.toLong()) {
            Int.ZERO.toLong()
        } else {
            totalOrder
        }
    }

    fun getNetIncomeValue(): Long {
        return if (netIncome < Int.ZERO.toLong()) {
            Int.ZERO.toLong()
        } else {
            netIncome
        }
    }
}
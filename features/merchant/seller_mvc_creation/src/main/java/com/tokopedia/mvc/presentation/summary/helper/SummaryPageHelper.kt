package com.tokopedia.mvc.presentation.summary.helper

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType

object SummaryPageHelper {
    fun getMaxExpenses(configuration: VoucherConfiguration): Long {
        return with(configuration) {
            if (benefitType == BenefitType.NOMINAL) {
                benefitIdr
            } else {
                benefitMax
            } * quota
        }
    }
}

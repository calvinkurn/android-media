package com.tokopedia.mvc.util.extension

import com.tokopedia.mvc.domain.entity.enums.BenefitType


fun BenefitType.isNominal(): Boolean {
    return this == BenefitType.NOMINAL
}

fun BenefitType.isPercentage(): Boolean {
    return this == BenefitType.PERCENTAGE
}

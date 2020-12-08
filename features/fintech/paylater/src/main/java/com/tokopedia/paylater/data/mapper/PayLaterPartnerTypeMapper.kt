package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.PayLaterItemProductData

const val APPLY_STEPS_PARTNER = "how_to_apply"
const val USAGE_STEPS_PARTNER = "how_to_use"

sealed class PayLaterPartnerType(val tag: String)
object RegisterStepsPartnerType: PayLaterPartnerType(APPLY_STEPS_PARTNER)
object UsageStepsPartnerType: PayLaterPartnerType(USAGE_STEPS_PARTNER)
object PayLaterPartnerTypeMapper {

    fun getPayLaterPartnerType(payLaterPartnerData: PayLaterItemProductData): PayLaterPartnerType {
        return if (payLaterPartnerData.isAbleToApply) {
            RegisterStepsPartnerType
        } else UsageStepsPartnerType
    }
}
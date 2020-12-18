package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData

const val APPLY_STEPS_PARTNER = "how_to_apply"
const val USAGE_STEPS_PARTNER = "how_to_use"
const val PROCESSING_APPLICATION_PARTNER = "application_in_process"

sealed class PayLaterPartnerType(val tag: String)
object RegisterStepsPartnerType: PayLaterPartnerType(APPLY_STEPS_PARTNER)
object ProcessingApplicationPartnerType: PayLaterPartnerType(PROCESSING_APPLICATION_PARTNER)
object UsageStepsPartnerType: PayLaterPartnerType(USAGE_STEPS_PARTNER)
object PayLaterPartnerTypeMapper {

    fun getPayLaterPartnerType(payLaterPartnerData: PayLaterItemProductData, partnerApplicationDetail: PayLaterApplicationDetail?): PayLaterPartnerType {
        var isApplicationActive = false
        var status : String = ""
        partnerApplicationDetail?.let {
            val payLaterStatus = PayLaterApplicationStatusMapper.getApplicationStatusType(it)
            isApplicationActive = payLaterStatus is PayLaterStatusActive
            status = payLaterStatus.tag
        }

        if (payLaterPartnerData.isAbleToApply && isApplicationActive) {
            return UsageStepsPartnerType
        } else if (payLaterPartnerData.isAbleToApply && status.isNotBlank()) {
            return ProcessingApplicationPartnerType
        } else if (payLaterPartnerData.isAbleToApply) {
            // isApplicationActive = false as partnerApplicationDetail == null
            return RegisterStepsPartnerType
        } else return UsageStepsPartnerType
    }

    fun getPayLaterApplicationDataForPartner(paymentOption: PayLaterItemProductData, applicationStatusList: ArrayList<PayLaterApplicationDetail>): PayLaterApplicationDetail? {
        for (applicationStatus in applicationStatusList) {
            if (applicationStatus.payLaterGatewayCode == paymentOption.gateWayCode) return applicationStatus
        }
        return null
    }
}
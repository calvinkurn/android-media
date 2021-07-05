package com.tokopedia.pdpsimulation.paylater.mapper

import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData

const val APPLY_STEPS_PARTNER = "how_to_apply"
const val USAGE_STEPS_PARTNER = "how_to_use"
const val PROCESSING_APPLICATION_PARTNER = "application_in_process"

sealed class PayLaterPartnerType(val tag: String)
object RegisterStepsPartnerType : PayLaterPartnerType(APPLY_STEPS_PARTNER)
object ProcessingApplicationPartnerType : PayLaterPartnerType(PROCESSING_APPLICATION_PARTNER)
object UsageStepsPartnerType : PayLaterPartnerType(USAGE_STEPS_PARTNER)
object PayLaterPartnerTypeMapper {

    fun getPayLaterPartnerType(
            payLaterPartnerData: PayLaterItemProductData,
            partnerApplicationDetail: PayLaterApplicationDetail?): PayLaterPartnerType {
        var isApplicationActive = false
        var status = STATUS_EMPTY
        partnerApplicationDetail?.let {
            val payLaterStatus = PayLaterApplicationStatusMapper.getApplicationStatusType(it)
            isApplicationActive = payLaterStatus is PayLaterStatusActive
            status = payLaterStatus.status
        }

        return when {
            payLaterPartnerData.isAbleToApply == false -> UsageStepsPartnerType
            isApplicationActive -> UsageStepsPartnerType
            status != STATUS_EMPTY -> ProcessingApplicationPartnerType
            else -> RegisterStepsPartnerType
        }
    }

    fun getPayLaterApplicationDataForPartner(
            paymentOption: PayLaterItemProductData,
            applicationStatusList: ArrayList<PayLaterApplicationDetail>,
    ): PayLaterApplicationDetail? {
        val partnerStatus = applicationStatusList.filter {
            it.payLaterGatewayCode == paymentOption.gateWayCode
        }
        return partnerStatus.getOrNull(0)
    }
}
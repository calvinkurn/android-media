package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.domain.model.PayLaterProductData

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
        var status = ""
        partnerApplicationDetail?.let {
            val payLaterStatus = PayLaterApplicationStatusMapper.getApplicationStatusType(it)
            isApplicationActive = payLaterStatus is PayLaterStatusActive || payLaterStatus is PayLaterStatusApproved
            status = payLaterStatus.tag
        }

        return if (payLaterPartnerData.isAbleToApply == true && isApplicationActive) {
            UsageStepsPartnerType
        } else if (payLaterPartnerData.isAbleToApply == true && status.isNotBlank()) {
            ProcessingApplicationPartnerType
        } else if (payLaterPartnerData.isAbleToApply == true) {
            // isApplicationActive = false as partnerApplicationDetail == null
            RegisterStepsPartnerType
        } else UsageStepsPartnerType
    }

    fun getPayLaterApplicationDataForPartner(
            paymentOption: PayLaterItemProductData,
            applicationStatusList: ArrayList<PayLaterApplicationDetail>): PayLaterApplicationDetail? {
        val partnerStatus = applicationStatusList.filter {
            it.payLaterGatewayCode == paymentOption.gateWayCode
        }
        return partnerStatus.getOrNull(0)
    }

    fun validateProductData(productDataList: PayLaterProductData?): PayLaterProductData? {
        if (productDataList == null) return null
        if (productDataList.productList.isNullOrEmpty()) return null
        return productDataList
    }
}
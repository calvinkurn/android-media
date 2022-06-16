package com.tokopedia.sellerorder.detail.presentation.mapper

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceResponse
import com.tokopedia.sellerorder.detail.presentation.model.MVCUsageUiModel

object SomDynamicPaymentResponseMapper {
    fun mapResponseToPaymentsUiModel(
        response: SomDynamicPriceResponse.GetSomDynamicPrice?
    ): SomDetailData {
        val paymentData = SomDetailPayments.PaymentDataUiModel(
            label = response?.paymentData?.label.orEmpty(),
            value = response?.paymentData?.value.orEmpty()
        )
        val paymentMethodList = mutableListOf<SomDetailPayments.PaymentMethodUiModel>()
        response?.paymentMethod?.map {
            paymentMethodList.add(
                SomDetailPayments.PaymentMethodUiModel(
                    label = it.label,
                    value = it.value
                )
            )
        }
        val pricingList = mutableListOf<SomDetailPayments.PricingData>()
        response?.pricingData?.map {
            pricingList.add(
                SomDetailPayments.PricingData(
                    label = it.label,
                    value = it.value
                )
            )
        }
        val dataPayments = SomDetailPayments(
            paymentDataUiModel = paymentData,
            paymentMethodUiModel = paymentMethodList,
            pricingData = pricingList
        )
        return SomDetailData(dataPayments, SomConsts.DETAIL_PAYMENT_TYPE)
    }

    fun mapResponseToMerchantVoucherUsageUiModel(
        response: SomDynamicPriceResponse.GetSomDynamicPrice?
    ): SomDetailData? {
        return response.let {
            val description = it?.promoShipping?.label.orEmpty()
            val value = it?.promoShipping?.value.orEmpty()
            val valueDetail = it?.promoShipping?.valueDetail.orEmpty()
            if (description.isNotBlank() && value.isNotBlank()) {
                val mvcData = MVCUsageUiModel(description, value, valueDetail)
                SomDetailData(mvcData, SomConsts.DETAIL_MVC_USAGE_TYPE)
            } else null
        }
    }
}
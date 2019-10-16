package com.tokopedia.product.detail.data.model.financing

import com.google.gson.annotations.SerializedName

data class FinancingDataResponse(
        @SerializedName("ft_installment_calculation")
        val ftInstallmentCalculation: FtInstallmentCalcualtionDataResponse = FtInstallmentCalcualtionDataResponse()
)

data class FtInstallmentCalcualtionDataResponse(
        @SerializedName("data")
        val ftInstallmentCalculation: FtInstallmentCalcualtionData = FtInstallmentCalcualtionData()
)

data class FtInstallmentCalcualtionData(
        @SerializedName("credit_card")
        val creditCardInstallmentData: ArrayList<FtCalculationPartnerData> = ArrayList(),

        @SerializedName("non_credit_card")
        val nonCreditCardInstallmentData: ArrayList<FtCalculationPartnerData> = ArrayList()
)

data class FtCalculationPartnerData(
        @SerializedName("partner_code")
        val partnerCode: String,

        @SerializedName("partner_name")
        val partnerName: String,

        @SerializedName("partner_icon")
        val partnerIcon: String,

        @SerializedName("installment_list")
        val creditCardInstallmentList: ArrayList<CalculationInstallmentData> = ArrayList(),

        @SerializedName("instruction_list")
        val creditCardInstructionList: ArrayList<PaymentPartnerInstructionData> = ArrayList()
)

data class CalculationInstallmentData(
        @SerializedName("term")
        val creditCardinstallmentTerm: Int,

        @SerializedName("mdr_value")
        val mdrValue: Float,

        @SerializedName("mdr_type")
        val mdrType: String,

        @SerializedName("interest_rate")
        val interestRate: Float,

        @SerializedName("minimum_amount")
        val minimumAmount: Int,

        @SerializedName("maximum_amount")
        val maximumAmount: Int,

        @SerializedName("monthly_price")
        val monthlyPrice: Float,

        @SerializedName("os_monthly_price")
        val crdeitCardinstallmentTerm: Float
)

data class PaymentPartnerInstructionData(
        @SerializedName("order")
        val order: Int,

        @SerializedName("description")
        val description: String,

        @SerializedName("ins_image_url")
        val insImageUrl: String
)
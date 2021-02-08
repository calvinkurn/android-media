package com.tokopedia.pdpsimulation.creditcard.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class CreditCardGetSimulationResponse(
        @SerializedName("cc_fetchpdpcreditcardsimulation")
        val pdpCreditCardSimulationResult: PdpCreditCardSimulation,
)

data class PdpCreditCardSimulation(
        @SerializedName("data")
        val creditCardGetSimulationResult: CreditCardSimulationResult?,
)

data class CreditCardSimulationResult(
        @SerializedName("cicilan")
        val creditCardInstallmentList: ArrayList<CreditCardInstallmentItem>?,
        @SerializedName("principal_amount")
        val principalAmount: Float?,
        @SerializedName("ticker_info")
        val tickerInformation: String?,
        @SerializedName("cta_main_label")
        val ctaMainLabelText: String?,
        @SerializedName("cta_description")
        val ctaDescriptionText: String?,
)

data class CreditCardInstallmentItem(
        @SerializedName("tenure_id")
        val tenureId: Int?,
        @SerializedName("tenure_desc")
        val tenureDescription: String?,
        @SerializedName("min_amount")
        val minAmount: Long?,
        @SerializedName("installment_amount")
        val installmentAmount: Float?,
        @SerializedName("total_bank")
        val totalBanks: Int?,
        @SerializedName("is_popular")
        val isPopular: Boolean?,
        @SerializedName("bank")
        val simulationBankList: ArrayList<SimulationBank>?,
        @SerializedName("is_disabled")
        val isDisabled: Boolean?,
        var isSelected: Boolean,
)

@Parcelize
data class SimulationBank(
        @SerializedName("bank_id")
        val bankId: Int?,
        @SerializedName("bank_name")
        val bankName: String?,
        @SerializedName("transaction_benefit")
        val transactionBenefits: String?,
        @SerializedName("available_duration")
        val availableDurationList: ArrayList<Int>?,
        @SerializedName("bank_image_url")
        val bankImageUrl: String?,
) : Parcelable
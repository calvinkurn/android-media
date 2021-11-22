package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class VoucherOrdersItem(
        @SerializedName("cashback_wallet_amount")
        val cashbackWalletAmount: Int = 0,

        @SerializedName("code")
        val code: String = "",

        @SerializedName("unique_id")
        val uniqueId: String = "",

        @SerializedName("discount_amount")
        val discountAmount: Int = 0,

        @SerializedName("title_description")
        val titleDescription: String = "",

        @SerializedName("is_po")
        val isPo: Int = 0,

        @SerializedName("message")
        val message: Message = Message(),

        @SerializedName("type")
        val type: String = "",

        @SerializedName("benefit_details")
        val benefitDetails: List<BenefitDetailsItem> = emptyList(),

        @SerializedName("success")
        val success: Boolean = false,

        @SerializedName("invoice_description")
        val invoiceDescription: String = ""
)
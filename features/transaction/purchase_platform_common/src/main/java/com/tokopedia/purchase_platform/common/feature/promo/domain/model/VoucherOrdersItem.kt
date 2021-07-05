package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class VoucherOrdersItem(
        @SerializedName("cashback_wallet_amount")
        val cashbackWalletAmount: Int? = null,

        @SerializedName("code")
        val code: String? = null,

        @SerializedName("unique_id")
        val uniqueId: String? = null,

        @SerializedName("discount_amount")
        val discountAmount: Int? = null,

        @SerializedName("title_description")
        val titleDescription: String? = null,

        @SerializedName("is_po")
        val isPo: Int? = null,

        @SerializedName("message")
        val message: Message? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("benefit_details")
        val benefitDetails: List<BenefitDetailsItem?>? = null,

        @SerializedName("success")
        val success: Boolean? = null,

        @SerializedName("invoice_description")
        val invoiceDescription: String? = null
)
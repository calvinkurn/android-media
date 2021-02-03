package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class VoucherOrdersItem(

		@field:SerializedName("cashback_wallet_amount")
	val cashbackWalletAmount: Int? = null,

		@field:SerializedName("code")
	val code: String? = null,

		@field:SerializedName("unique_id")
	val uniqueId: String? = null,

		@field:SerializedName("discount_amount")
	val discountAmount: Int? = null,

		@field:SerializedName("title_description")
	val titleDescription: String? = null,

		@field:SerializedName("is_po")
	val isPo: Int? = null,

		@field:SerializedName("message")
	val message: Message? = null,

		@field:SerializedName("type")
	val type: String? = null,

		@field:SerializedName("benefit_details")
	val benefitDetails: List<BenefitDetailsItem?>? = null,

		@field:SerializedName("success")
	val success: Boolean? = null,

		@field:SerializedName("invoice_description")
	val invoiceDescription: String? = null
)
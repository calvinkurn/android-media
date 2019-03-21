package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

	@field:SerializedName("codes")
	val codes: List<String?>? = null,

	@field:SerializedName("promo_code_id")
	val promoCodeId: Int? = null,

	@field:SerializedName("voucher_orders")
	val voucherOrders: List<VoucherOrdersItem?>? = null,

	@field:SerializedName("cashback_advocate_referral_amount")
	val cashbackAdvocateReferralAmount: Int? = null,

	@field:SerializedName("clashing_info_detail")
	val clashingInfoDetail: ClashingInfoDetail? = null,

	@field:SerializedName("cashback_wallet_amount")
	val cashbackWalletAmount: Int? = null,

	@field:SerializedName("discount_amount")
	val discountAmount: Int? = null,

	@field:SerializedName("title_description")
	val titleDescription: String? = null,

	@field:SerializedName("global_success")
	val globalSuccess: Boolean? = null,

	@field:SerializedName("message")
	val message: Message? = null,

	@field:SerializedName("gateway_id")
	val gatewayId: String? = null,

	@field:SerializedName("is_coupon")
	val isCoupon: Int? = null,

	@field:SerializedName("coupon_description")
	val couponDescription: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("invoice_description")
	val invoiceDescription: String? = null,

	@field:SerializedName("benefit_summary_info")
	val benefitSummaryInfo: BenefitSummaryInfo? = null,

	@field:SerializedName("cashback_voucher_description")
	val cashbackVoucherDescription: String? = null
)
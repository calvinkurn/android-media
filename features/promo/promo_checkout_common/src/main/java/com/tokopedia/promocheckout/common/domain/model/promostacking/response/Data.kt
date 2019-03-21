package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

        @field:SerializedName("codes")
        val codes: List<String?>? = ArrayList(),

        @field:SerializedName("promo_code_id")
        val promoCodeId: Int? = 0,

        @field:SerializedName("voucher_orders")
        val voucherOrders: List<VoucherOrdersItem?>? = ArrayList(),

        @field:SerializedName("cashback_advocate_referral_amount")
        val cashbackAdvocateReferralAmount: Int? = 0,

        @field:SerializedName("clashing_info_detail")
        val clashingInfoDetail: ClashingInfoDetail? = ClashingInfoDetail(),

        @field:SerializedName("cashback_wallet_amount")
        val cashbackWalletAmount: Int? = 0,

        @field:SerializedName("discount_amount")
        val discountAmount: Int? = 0,

        @field:SerializedName("title_description")
        val titleDescription: String? = "",

        @field:SerializedName("global_success")
        val globalSuccess: Boolean? = false,

        @field:SerializedName("message")
        val message: Message? = Message(),

        @field:SerializedName("gateway_id")
        val gatewayId: String? = "",

        @field:SerializedName("is_coupon")
        val isCoupon: Int? = 0,

        @field:SerializedName("coupon_description")
        val couponDescription: String? = "",

        @field:SerializedName("success")
        val success: Boolean? = false,

        @field:SerializedName("invoice_description")
        val invoiceDescription: String? = "",

        @field:SerializedName("cashback_voucher_description")
        val cashbackVoucherDescription: String? = ""
)
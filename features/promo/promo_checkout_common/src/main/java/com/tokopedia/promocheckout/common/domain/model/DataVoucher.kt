package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataVoucher {

    @SerializedName("code")
    @Expose
    var code: String = ""
    @SerializedName("promo_code_id")
    @Expose
    var promoCodeId: Int = 0
    @SerializedName("discount_amount")
    @Expose
    var discountAmount: String = ""
    @SerializedName("cashback_amount")
    @Expose
    var cashbackAmount: Long = 0L
    @SerializedName("saldo_amount")
    @Expose
    var saldoAmount: Int = 0
    @SerializedName("cashback_top_cash_amount")
    @Expose
    var cashbackTopCashAmount: Int = 0
    @SerializedName("cashback_voucher_amount")
    @Expose
    var cashbackVoucherAmount: Int = 0
    @SerializedName("cashback_advocate_referral_amount")
    @Expose
    var cashbackAdvocateReferralAmount: Int = 0
    @SerializedName("extra_amount")
    @Expose
    var extraAmount: Int = 0
    @SerializedName("cashback_voucher_description")
    @Expose
    var cashbackVoucherDescription: String = ""
    @SerializedName("gateway_id")
    @Expose
    var gatewayId: String = ""
    @SerializedName("token")
    @Expose
    var token: String = ""
    @SerializedName("message")
    @Expose
    var message: Message = Message()
    @SerializedName("is_coupon")
    @Expose
    var isCoupon: Int = 0
    @SerializedName("title_description")
    @Expose
    var titleDescription: String = ""

}

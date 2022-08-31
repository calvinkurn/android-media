package com.tokopedia.promocheckout.common.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataVoucher {

    @SerializedName("code")
    @Expose
    var code: String = ""
    @SerializedName("promo_code_id")
    @Expose
    var promoCodeId: String = ""
    @SerializedName("discount_amount")
    @Expose
    var discountAmount: String = ""
    @SerializedName("cashback_amount")
    @Expose
    var cashbackAmount: Long = 0L
    @SerializedName("saldo_amount")
    @Expose
    var saldoAmount: Long = 0L
    @SerializedName("cashback_top_cash_amount")
    @Expose
    var cashbackTopCashAmount: Long = 0L
    @SerializedName("cashback_voucher_amount")
    @Expose
    var cashbackVoucherAmount: Long = 0L
    @SerializedName("cashback_advocate_referral_amount")
    @Expose
    var cashbackAdvocateReferralAmount: Long = 0L
    @SerializedName("extra_amount")
    @Expose
    var extraAmount: Long = 0L
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

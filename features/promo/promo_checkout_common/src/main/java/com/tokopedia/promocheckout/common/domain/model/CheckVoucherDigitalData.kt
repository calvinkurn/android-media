package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckVoucherDigitalData {

    @SerializedName("success")
    @Expose
    var success: Boolean = false
    @SerializedName("message")
    @Expose
    var message: Message = Message()
    @SerializedName("code")
    @Expose
    var code: String = ""
    @SerializedName("promo_code_id")
    @Expose
    var promoCodeId: Int = 0
    @SerializedName("discount_amount")
    @Expose
    var discountAmount: Long = 0
    @SerializedName("cashback_amount")
    @Expose
    var cashbackAmount: Long = 0
    @SerializedName("saldo_amount")
    @Expose
    var saldoAmount: Long = 0
    @SerializedName("cashback_top_cash_amount")
    @Expose
    var cashbackTopCashAmount: Long = 0
    @SerializedName("cashback_voucher_amount")
    @Expose
    var cashbackVoucherAmount: Long = 0
    @SerializedName("invoice_description")
    @Expose
    var invoiceDescription: String = ""
    @SerializedName("gateway_id")
    @Expose
    var gatewayId: String = ""
    @SerializedName("promo_id")
    @Expose
    var promoId: String = ""
    @SerializedName("is_coupon")
    @Expose
    var isCoupon: Int = 0
    @SerializedName("title_description")
    @Expose
    var titleDescription: String = ""
    @SerializedName("message_success")
    @Expose
    var successMessage: String = ""

}

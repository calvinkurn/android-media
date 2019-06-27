package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/7/17.
 */

class AttributesVoucher {
    @SerializedName("voucher_code")
    @Expose
    var voucherCode: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int = 0
    @SerializedName("discount_amount")
    @Expose
    var discountAmount: String? = null
    @SerializedName("discount_amount_plain")
    @Expose
    var discountAmountPlain: Int = 0
    @SerializedName("cashback_amount")
    @Expose
    var cashbackAmount: String? = null
    @SerializedName("cashback_amount_plain")
    @Expose
    var cashbackAmountPlain: Int = 0
    @SerializedName("message")
    @Expose
    var message: String? = null
}

package com.tokopedia.topchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Voucher {

    @SerializedName("voucher_id")
    @Expose
    var voucherId: Int = 0
    @SerializedName("tnc")
    @Expose
    var tnc = ""
    @SerializedName("voucher_code")
    @Expose
    var voucherCode: String = ""
    @SerializedName("voucher_name")
    @Expose
    var voucherName: String = ""
    @SerializedName("minimum_spend")
    @Expose
    var minimumSpend: Int = 0
    @SerializedName("valid_thru")
    @Expose
    var validThru: Long = 0
    @SerializedName("valid_trhu_str")
    @Expose
    var validTrhuStr = ""
    @SerializedName("desktop_url")
    @Expose
    var desktopUrl = ""
    @SerializedName("mobile_url")
    @Expose
    var mobileUrl = ""
    @SerializedName("amount")
    @Expose
    var amount: Float = 0f
    @SerializedName("amount_type")
    @Expose
    var amountType: Int = 0
    @SerializedName("identifier")
    @Expose
    var identifier = ""
    @SerializedName("voucher_type")
    @Expose
    var voucherType: Int = 0
    @SerializedName("owner_id")
    @Expose
    var ownerId: Int = 0
}

package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory

data class Voucher(
        @SerializedName("amount")
        val amount: Amount = Amount(),
        @SerializedName("banner")
        val banner: Banner = Banner(),
        @SerializedName("minimum_spend")
        val minimumSpend: Int = 0,
        @SerializedName("tnc")
        val tnc: String = "",
        @SerializedName("valid_thru")
        val validThru: String = "",
        @SerializedName("voucher_code")
        val voucherCode: String = "",
        @SerializedName("voucher_name")
        val voucherName: String = "",
        @SerializedName("voucher_type")
        val voucherType: VoucherType = VoucherType()
) : Visitable<AttachVoucherTypeFactory> {
    override fun type(typeFactory: AttachVoucherTypeFactory): Int {
        return typeFactory.type(this)
    }
}
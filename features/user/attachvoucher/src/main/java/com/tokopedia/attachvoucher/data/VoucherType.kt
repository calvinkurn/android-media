package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName

data class VoucherType(
        @SerializedName("identifier")
        val identifier: String = "",
        @SerializedName("voucher_type")
        val voucherType: Int = 0
) {
        companion object {
                const val FREE_ONGKIR = 1
                const val DISCOUNT = 2
                const val CASH_BACK = 3
        }
}
package com.tokopedia.attachcommon.data

data class VoucherPreview(
        val source: String = "",
        val voucherId: Int = -1,
        val tnc: String = "",
        val voucher_code: String = "",
        val voucher_name: String = "",
        val minimum_spend: Int = -1,
        val valid_thru: Long = -1,
        val desktop_url: String = "",
        val mobile_url: String = "",
        val amount: Int = -1,
        val amount_type: Int = -1,
        val identifier: String = "",
        val voucher_type: Int = -1
)
package com.tokopedia.attachcommon.data

data class VoucherPreview(
        val source: String = "",
        val voucherId: Int = -1,
        val tnc: String = "",
        val voucherCode: String = "",
        val voucherName: String = "",
        val minimumSpend: Int = -1,
        val validThru: Long = -1,
        val desktopUrl: String = "",
        val mobileUrl: String = "",
        val amount: Int = -1,
        val amountType: Int = -1,
        val identifier: String = "",
        val voucherType: Int = -1,
        val isPublic: Int = 1,
        val isLockToProduct: Int = 0,
        var applink: String = "",
        var weblink: String = ""
) {
        fun isLockToProduct() = isLockToProduct == 1
}
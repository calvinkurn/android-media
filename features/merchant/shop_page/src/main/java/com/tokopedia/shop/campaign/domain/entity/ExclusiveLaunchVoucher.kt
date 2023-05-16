package com.tokopedia.shop.campaign.domain.entity

data class ExclusiveLaunchVoucher(
    val id: Long,
    val voucherName: String,
    val minimumPurchase: Long,
    val remainingQuota: Int,
    val source: VoucherSource
) {
    /**
     * CATALOG = Voucher need to be claimed first before use
     * MERCHANT_CREATED = No need to claim voucher first before use
     */
    enum class VoucherSource {
        PROMO,
        MERCHANT_CREATED
    }
}

package com.tokopedia.shop.campaign.domain.entity

data class ExclusiveLaunchVoucher(
    val id: Long,
    val voucherName: String,
    val minimumPurchase: Long,
    val remainingQuota: Int,
    val source: VoucherSource,
    val slug: String
) {
    /**
     * Promo = Voucher need to be claimed first before use
     * MerchantCreated = No need to claim voucher first before use
     */
    sealed class VoucherSource {
        data class Promo(val isClaimed: Boolean, val voucherCode: String): VoucherSource()
        data class MerchantCreated(val eligibleProductIds: List<String>) : VoucherSource()
    }
}

fun ExclusiveLaunchVoucher.isMerchantVoucher(): Boolean {
    return this.source is ExclusiveLaunchVoucher.VoucherSource.MerchantCreated
}

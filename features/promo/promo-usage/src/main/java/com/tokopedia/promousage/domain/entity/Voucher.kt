package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class Voucher(
    val id: Long,
    val benefitAmount: Long,
    val termAndCondition: String,
    val expiredDate: String,
    val iconImageUrl: String,
    val superGraphicImageUrl: String,
    val voucherType: VoucherType,
    val voucherState: VoucherState,
    val voucherSource: VoucherSource = VoucherSource.Promo,
    val shouldVisible: Boolean
) : DelegateAdapterItem {
    override fun id() = id
}

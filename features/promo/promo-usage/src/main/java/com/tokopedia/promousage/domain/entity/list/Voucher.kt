package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.domain.entity.VoucherSource
import com.tokopedia.promousage.domain.entity.VoucherState
import com.tokopedia.promousage.domain.entity.VoucherType
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
    val visible: Boolean
) : DelegateAdapterItem {
    override fun id() = id
}

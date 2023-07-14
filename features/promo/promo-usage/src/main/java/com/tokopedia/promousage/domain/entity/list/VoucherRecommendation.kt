package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class VoucherRecommendation(
    val title: String,
    val vouchers: List<Voucher>
) : DelegateAdapterItem {
    override fun id() = vouchers
}

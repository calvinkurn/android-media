package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class VoucherAccordion(
    val title: String,
    val isExpanded: Boolean,
    val vouchers: List<DelegateAdapterItem>
) : DelegateAdapterItem {
    override fun id() = title
}

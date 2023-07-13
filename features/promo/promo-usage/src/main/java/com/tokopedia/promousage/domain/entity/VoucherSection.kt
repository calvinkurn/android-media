package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class VoucherSection(
    val title: String,
    val isExpanded: Boolean,
    val vouchers : List<DelegateAdapterItem>
)
